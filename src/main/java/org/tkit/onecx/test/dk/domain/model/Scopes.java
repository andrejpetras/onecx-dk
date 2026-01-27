package org.tkit.onecx.test.dk.domain.model;

import java.util.List;

public interface Scopes {

    String OPENID = "openid";
    String PROFILE = "profile";
    String EMAIL = "email";
    String OFFLINE_ACCESS = "offline_access";

    static List<String> supportedScopes() {
        return List.of(OPENID, PROFILE, EMAIL, OFFLINE_ACCESS);
    }
}
