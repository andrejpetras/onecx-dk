package org.tkit.onecx.test.dk.domain.model;

import java.util.List;

public interface ResponseTypes {

    String NONE = "none";
    String CODE = "code";
    String ID_TOKEN = "id_token";
    String TOKEN = "token";
    String ID_TOKEN_TOKEN = ID_TOKEN + " " + TOKEN;
    String TOKEN_ID_TOKEN = TOKEN + " " + ID_TOKEN;

    static List<String> supportedResponseTypes() {
        return List.of(NONE, CODE, ID_TOKEN, TOKEN, ID_TOKEN_TOKEN);
    }
}
