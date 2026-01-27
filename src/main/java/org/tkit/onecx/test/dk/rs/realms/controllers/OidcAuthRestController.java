package org.tkit.onecx.test.dk.rs.realms.controllers;

import java.net.URI;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.tkit.onecx.test.dk.domain.model.AuthorizationCode;
import org.tkit.onecx.test.dk.domain.model.ResponseTypes;
import org.tkit.onecx.test.dk.domain.services.IssuerService;
import org.tkit.onecx.test.dk.domain.services.RealmService;
import org.tkit.onecx.test.dk.domain.services.TokenService;
import org.tkit.onecx.test.dk.domain.utils.ScopeUtils;

import gen.org.tkit.onecx.test.dk.rs.realms.AuthApi;
import gen.org.tkit.onecx.test.dk.rs.realms.model.OAuthErrorDTO;

@ApplicationScoped
public class OidcAuthRestController implements AuthApi {

    @Inject
    RealmService realmService;

    @Inject
    TokenService tokenService;

    @Inject
    UriInfo uriInfo;

    @Inject
    IssuerService issuerService;

    @Override
    public Response authorize(String realm, String responseType, String clientId, URI redirectUri, String scope, String state,
            String nonce, String codeChallenge, String codeChallengeMethod, String asUser) {

        if (responseType == null || clientId == null || redirectUri == null) {
            return bad(OAuthErrorDTO.ErrorEnum.INVALID_REQUEST);
        }

        var store = realmService.getRealm(realm);

        if (store == null) {
            return bad(OAuthErrorDTO.ErrorEnum.REALM_NOT_FOUND);
        }

        var client = store.getClient(clientId);
        if (client == null) {
            return bad(OAuthErrorDTO.ErrorEnum.UNAUTHORIZED_CLIENT);
        }

        if (!client.getRedirectUris().contains("*")) {
            if (!client.getRedirectUris().contains(redirectUri.toString())) {
                return bad(OAuthErrorDTO.ErrorEnum.INVALID_REQUEST);
            }
        }

        if (asUser == null) {
            String returnTo = String.format(
                    "/realms/%s/protocol/openid-connect/auth?response_type=%s&client_id=%s&redirect_uri=%s&scope=%s&state=%s&nonce=%s&code_challenge=%s&code_challenge_method=%s",
                    realm,
                    enc(responseType), enc(clientId), enc(redirectUri.toString()), enc(scope), enc(state), enc(nonce),
                    enc(codeChallenge),
                    enc(codeChallengeMethod));
            return Response
                    .seeOther(URI
                            .create(String.format("/realms/%s/login-actions/authenticate?return_to=%s", realm, enc(returnTo))))
                    .build();
        }

        var user = store.getUser(asUser);
        if (user == null || !user.isEnabled()) {
            return bad(OAuthErrorDTO.ErrorEnum.ACCESS_DENIED);
        }
        Set<String> scopes = ScopeUtils.toScopes(scope);

        responseType = responseType.toLowerCase();

        if (ResponseTypes.CODE.equals(responseType)) {
            AuthorizationCode ac = new AuthorizationCode();
            ac.setCode(UUID.randomUUID().toString());
            ac.setClientId(clientId);
            ac.setUsername(user.getUsername());
            ac.setRedirectUri(redirectUri.toString());
            ac.setNonce(nonce);
            ac.setCodeChallenge(codeChallenge);
            ac.setCodeChallengeMethod(codeChallengeMethod);
            ac.setScopes(scopes);
            ac.setExpiresAt(Instant.now().plusSeconds(300));
            store.saveAuthCode(ac);

            var tmp = URI.create(redirectUri + (redirectUri.toString().contains("?") ? "&" : "?") + "code=" + ac.getCode()
                    + (state != null ? "&state=" + enc(state) : ""));
            return Response.seeOther(tmp).build();
        }

        if (ResponseTypes.TOKEN.equals(responseType) ||
                ResponseTypes.ID_TOKEN.equals(responseType) ||
                ResponseTypes.ID_TOKEN_TOKEN.equals(responseType) ||
                ResponseTypes.TOKEN_ID_TOKEN.equals(responseType)) {

            String fragment = "";

            var issuer = issuerService.issuer(uriInfo, realm);

            if (responseType.contains(ResponseTypes.TOKEN)) {
                String at = tokenService.createAccessToken(issuer, user, client, scopes);
                fragment += "access_token=" + at + "&token_type=bearer&expires_in=3600";
            }

            if (responseType.contains(ResponseTypes.ID_TOKEN)) {
                String idt = tokenService.createIdToken(issuer, user, client, nonce);
                fragment += (fragment.isEmpty() ? "" : "&") + "id_token=" + idt;
            }

            if (state != null) {
                fragment += (fragment.isEmpty() ? "" : "&") + "state=" + enc(state);
            }
            return Response.seeOther(URI.create(redirectUri + "#" + fragment)).build();
        }

        return bad(OAuthErrorDTO.ErrorEnum.INVALID_REQUEST);
    }

    private static String enc(String s) {
        return s == null ? "" : java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static Response bad(OAuthErrorDTO.ErrorEnum error) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new OAuthErrorDTO().error(error)).build();
    }

    @Override
    public Response doLogin(String realm, String username, String password, String redirectUri) {

        var store = realmService.getRealm(realm);

        if (store == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("realm not found").build();
        }

        var user = store.getUser(username);
        if (user == null || !user.isEnabled() || !user.getPassword().equals(password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }

        if (redirectUri == null) {
            return Response.seeOther(URI.create("/")).build();
        }
        String sep = redirectUri.contains("?") ? "&" : "?";
        return Response.seeOther(URI.create(redirectUri + sep + "as_user=" + username)).build();
    }

    @Override
    public Response loginPage(String realm, String redirectUri) {
        return Response.ok(
                """
                        <html>
                            <body>
                                <h3>Login %s</h3>
                                <form>
                                  <input type="hidden" name="redirectUri" value="%s"/>
                                  <label>Username <input name="username"/></label>
                                  <br/>
                                  <label>Password <input type="password" name="password"/></label>
                                  <br/>
                                  <br/>
                                  <button type="submit">Sign in</button>
                                </form>
                            </body>
                        </html>
                        """.formatted(realm, redirectUri == null ? "" : redirectUri)).build();
    }
}
