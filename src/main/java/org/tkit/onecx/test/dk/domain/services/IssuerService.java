package org.tkit.onecx.test.dk.domain.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ApplicationScoped
public class IssuerService {

    public String issuer(UriInfo uriInfo, String realm) {
        return uriInfo.getBaseUri() + "realms/" + realm;
    }
}
