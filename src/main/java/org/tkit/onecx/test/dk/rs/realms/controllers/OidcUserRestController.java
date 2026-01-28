package org.tkit.onecx.test.dk.rs.realms.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.tkit.onecx.test.dk.domain.model.ClaimNames;
import org.tkit.onecx.test.dk.domain.services.IssuerService;
import org.tkit.onecx.test.dk.domain.services.TokenService;

import gen.org.tkit.onecx.test.dk.rs.realms.UserApi;
import gen.org.tkit.onecx.test.dk.rs.realms.model.ErrorTokenDTO;
import gen.org.tkit.onecx.test.dk.rs.realms.model.UserInfoDTO;

@ApplicationScoped
public class OidcUserRestController implements UserApi {

    @Inject
    UriInfo uriInfo;

    @Context
    HttpHeaders headers;

    @Inject
    TokenService refreshTokenService;

    @Inject
    IssuerService issuerService;

    @Override
    public Response getUserinfo(String realm) {
        var auth = headers.getHeaderString("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorTokenDTO().error(ErrorTokenDTO.ErrorEnum.MISSING_BEARER_TOKEN)).build();
        }
        String token = auth.substring("Bearer ".length());

        try {
            var claims = refreshTokenService.parse(issuerService.issuer(uriInfo, realm), token);

            var dto = new UserInfoDTO().sub(claims.getSubject());
            if (claims.hasClaim(ClaimNames.EMAIL_VERIFIED)) {
                dto.setEmailVerified(claims.getClaimValue(ClaimNames.EMAIL_VERIFIED, Boolean.class));
            }
            if (claims.hasClaim(ClaimNames.NAME)) {
                dto.setName(claims.getClaimValueAsString(ClaimNames.NAME));
            }
            if (claims.hasClaim(ClaimNames.PREFERRED_USERNAME)) {
                dto.setPreferredUsername(claims.getClaimValueAsString(ClaimNames.PREFERRED_USERNAME));
            }
            if (claims.hasClaim(ClaimNames.GIVEN_NAME)) {
                dto.setGivenName(claims.getClaimValueAsString(ClaimNames.GIVEN_NAME));
            }
            if (claims.hasClaim(ClaimNames.FAMILY_NAME)) {
                dto.setFamilyName(claims.getClaimValueAsString(ClaimNames.FAMILY_NAME));
            }
            if (claims.hasClaim(ClaimNames.EMAIL)) {
                dto.setEmail(claims.getClaimValueAsString(ClaimNames.EMAIL));
            }
            return Response.ok(dto).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorTokenDTO().error(ErrorTokenDTO.ErrorEnum.INVALID_TOKEN).errorDescription(e.getMessage()))
                    .build();
        }
    }
}
