package org.tkit.onecx.test.dk.rs.realms.controllers;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.tkit.onecx.test.dk.config.DkConfig;
import org.tkit.onecx.test.dk.domain.model.Client;
import org.tkit.onecx.test.dk.domain.model.Realm;
import org.tkit.onecx.test.dk.domain.model.User;
import org.tkit.onecx.test.dk.domain.services.KeyManager;
import org.tkit.onecx.test.dk.domain.services.RealmService;

import gen.org.tkit.onecx.test.dk.rs.realms.OidcApi;
import gen.org.tkit.onecx.test.dk.rs.realms.model.OpenIdConfigurationDTO;
import gen.org.tkit.onecx.test.dk.rs.realms.model.TokenSuccessDTO;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;

@ApplicationScoped
public class OidcRestController implements OidcApi {

    @Inject
    DkConfig config;

    @Inject
    UriInfo uriInfo;

    @Inject
    RealmService realmService;

    @Inject
    KeyManager keyManager;

    private String issuer(String realm) {
        return uriInfo.getBaseUri() + "realms/" + realm;
    }

    @Override
    public Response getOpenIdConfiguration(String realm) {

        var base = issuer(realm);

        var result = new OpenIdConfigurationDTO()
                .issuer(URI.create(base))
                .authorizationEndpoint(URI.create(base + "/protocol/openid-connect/auth"))
                .tokenEndpoint(URI.create(base + "/protocol/openid-connect/token"))
                .jwksUri(URI.create(base + "/protocol/openid-connect/certs"))
                .userinfoEndpoint(URI.create(base + "/protocol/openid-connect/userinfo"))
                .endSessionEndpoint(URI.create(base + "/protocol/openid-connect/logout"))
                .checkSessionIframe(URI.create(base + "/protocol/openid-connect/login-status-iframe.html"))
                .scopesSupported(List.of("openid", "profile", "email", "roles"))
                .responseTypesSupported(List.of("none", "code", "id_token", "token", "id_token token",
                        "code id_token", "code token", "code id_token token"))
                .grantTypesSupported(
                        List.of("authorization_code", "refresh_token", "implicit", "password", "client_credentials"))
                .subjectTypesSupported(List.of(OpenIdConfigurationDTO.SubjectTypesSupportedEnum.PUBLIC))
                .idTokenEncryptionAlgValuesSupported(List.of(SignatureAlgorithm.RS256.name()))
                .codeChallengeMethodsSupported(List.of(OpenIdConfigurationDTO.CodeChallengeMethodsSupportedEnum.PLAIN,
                        OpenIdConfigurationDTO.CodeChallengeMethodsSupportedEnum.S256))
                .tokenEndpointAuthMethodsSupported(
                        List.of(OpenIdConfigurationDTO.TokenEndpointAuthMethodsSupportedEnum.CLIENT_SECRET_BASIC,
                                OpenIdConfigurationDTO.TokenEndpointAuthMethodsSupportedEnum.CLIENT_SECRET_POST));
        return Response.ok(result).build();
    }

    @Override
    public Response getToken(String realm, String grantType, String clientId, String clientSecret, String username,
            String password, String scope, String code, String redirectUri, String codeVerifier) {

        if (clientId == null || grantType == null) {
            return error(Response.Status.BAD_REQUEST, "invalid_request", "client_id and grant_type are required");
        }

        var store = realmService.getRealm(realm);

        if (store == null) {
            return error(Response.Status.BAD_REQUEST, "invalid_request", "realm does not exist");
        }

        if (!store.hasClient(clientId)) {
            return error(Response.Status.UNAUTHORIZED, "invalid_client", "Unknown client");
        }
        var client = store.getClient(clientId);

        return switch (grantType) {
            case "client_credentials" -> grandTypeClientCredentials(store, client, scope);
            case "password" -> grandTypePassword(store, client, scope, username, password);
            case "authorization_code" -> grandTypeAuthorizationCode(store, client, code, redirectUri, codeVerifier);
            default -> grandTypeDefault();
        };
    }

    private Response grandTypeDefault() {
        return error(Response.Status.BAD_REQUEST, "unsupported_grant_type",
                "Supported: authorization_code, password, client_credentials");
    }

    private Response grandTypePassword(Realm store, Client client, String scope, String username, String password) {

        if (username == null || password == null) {
            return error(Response.Status.BAD_REQUEST, "invalid_request", "username and password required");
        }

        if (!store.hasUser(username)) {
            return error(Response.Status.UNAUTHORIZED, "invalid_grant", "Invalid user");
        }
        var user = store.getUser(username);

        if (!password.equals(user.getPassword())) {
            return error(Response.Status.UNAUTHORIZED, "invalid_grant", "Invalid credentials");
        }

        Set<String> scopes = parseScopes(scope, client);

        return issueTokens(store, client, user, scopes, null);
    }

    private Response grandTypeAuthorizationCode(Realm store, Client client, String code, String redirectUri,
            String codeVerifier) {

        if (code == null || redirectUri == null) {
            return error(Response.Status.BAD_REQUEST, "invalid_request", "code and redirect_uri required");
        }

        var tmp = store.getAuthCode(code);

        if (tmp.isEmpty()) {
            return error(Response.Status.BAD_REQUEST, "invalid_grant", "Invalid or expired code");
        }

        var ac = tmp.get();
        if (!ac.getClientId().equals(client.getClientId())) {
            return error(Response.Status.BAD_REQUEST, "invalid_grant", "Code not for this client");
        }
        if (!ac.getRedirectUri().equals(redirectUri)) {
            return error(Response.Status.BAD_REQUEST, "invalid_grant", "redirect_uri mismatch");
        }

        if (ac.getCodeChallenge() != null) {

            if (codeVerifier == null || codeVerifier.isBlank()) {
                return error(Response.Status.BAD_REQUEST, "invalid_grant", "code_verifier required");
            }

            String method = ac.getCodeChallengeMethod() == null ? "plain" : ac.getCodeChallengeMethod();
            String derived;
            if ("S256".equalsIgnoreCase(method)) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
                    derived = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
                } catch (Exception e) {
                    return error(Response.Status.BAD_REQUEST, "server_error", e.getMessage());
                }
            } else {
                derived = codeVerifier;
            }
            if (!derived.equals(ac.getCodeChallenge())) {
                return error(Response.Status.BAD_REQUEST, "invalid_grant", "PKCE verification failed");
            }
        } else if (!client.isConfidential()) {
            return error(Response.Status.BAD_REQUEST, "invalid_grant", "PKCE required for public clients");
        }

        store.consumeAuthCode(code);

        if (!store.hasUser(ac.getUsername())) {
            return error(Response.Status.UNAUTHORIZED, "invalid_grant", "User not found");
        }
        var user = store.getUser(ac.getUsername());

        Set<String> scopes = new HashSet<>();
        if (ac.getScopes() != null) {
            scopes.addAll(ac.getScopes());
        }
        return issueTokens(store, client, user, scopes, ac.getNonce());
    }

    private Response grandTypeClientCredentials(Realm store, Client client, String scope) {
        Set<String> scopes = parseScopes(scope, client);
        return issueTokens(store, client, null, scopes, null);
    }

    private Response error(Response.Status s, String code, String desc) {
        return Response.status(s).entity(java.util.Map.of("error", code, "error_description", desc)).build();
    }

    private Set<String> parseScopes(String scopeParam, Client client) {
        Set<String> req = new HashSet<>();
        if (scopeParam != null && !scopeParam.isBlank()) {
            req.addAll(Arrays.asList(scopeParam.split(" ")));
        }
        if (client.getScopes() != null && !client.getScopes().isEmpty()) {
            req.retainAll(client.getScopes());
        }
        return req;
    }

    private Response issueTokens(Realm store, Client client, User user, Set<String> scopes, String nonce) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(config.oidc().tokenLifetime());

        JwtClaimsBuilder access = Jwt.claims();
        access.issuer(issuer(store.getName()))
                .issuedAt(now.getEpochSecond())
                .expiresAt(exp.getEpochSecond())
                .claim("jti", UUID.randomUUID().toString())
                .audience(client.getClientId());

        if (user != null) {
            access.groups(user.getGroups());
            access.subject(user.getId()).claim("preferred_username", user.getUsername());

            access.claim("realm_access", Map.of("roles", store.filterRole(user.getRoles())));

        } else {
            access.subject("client:" + client.getClientId()).claim("client_id", client.getClientId());
        }

        String claimScope = null;
        if (scopes != null && !scopes.isEmpty()) {
            claimScope = String.join(" ", scopes);
        }
        if (claimScope != null) {
            access.claim("scope", claimScope);
        }

        var accessToken = access.jws().keyId(keyManager.getKid()).sign(keyManager.getPrivateKey());

        var dto = new TokenSuccessDTO()
                .accessToken(accessToken)
                .expiresIn(config.oidc().tokenLifetime())
                .tokenType(TokenSuccessDTO.TokenTypeEnum.BEARER);

        if (claimScope != null) {
            dto.scope(claimScope);
        }

        if (user != null && scopes.contains("openid")) {
            JwtClaimsBuilder id = Jwt.claims();
            id.issuer(issuer(store.getName()))
                    .subject(user.getId())
                    .issuedAt(now.getEpochSecond())
                    .expiresAt(exp.getEpochSecond())
                    .audience(client.getClientId())
                    .claim("preferred_username", user.getUsername());
            if (nonce != null) {
                id.claim("nonce", nonce);
            }
            String idToken = id.jws().keyId(keyManager.getKid()).sign(keyManager.getPrivateKey());
            dto.idToken(idToken);
        }
        return Response.ok(dto).build();
    }

}
