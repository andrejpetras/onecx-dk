package org.tkit.onecx.test.dk.domain.model;

import java.util.List;

public interface ResponseTypes {

    String NONE = "none";
    String CODE = "code";
    String ID_TOKEN = "id_token";
    String TOKEN = "token";
    String ID_TOKEN_TOKEN = ID_TOKEN + " " + TOKEN;
    String CODE_TOKEN = CODE + " " + TOKEN;
    String CODE_ID_TOKEN = CODE + " " + ID_TOKEN;
    String CODE_ID_TOKEN_TOKEN = CODE + " " + ID_TOKEN + " " + TOKEN;

    static List<String> supportedResponseTypes() {
        return List.of(NONE, CODE, ID_TOKEN, TOKEN, ID_TOKEN_TOKEN, CODE_TOKEN, CODE_ID_TOKEN, CODE_ID_TOKEN_TOKEN);
    }
}
