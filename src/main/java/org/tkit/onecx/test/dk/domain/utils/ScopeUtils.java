package org.tkit.onecx.test.dk.domain.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ScopeUtils {

    public static Set<String> toScopes(String s) {
        if (s == null || s.isBlank()) {
            return Collections.emptySet();
        }
        return new HashSet<>(Arrays.asList(s.split(" ")));
    }

    public static String fromScopes(Set<String> scopes) {
        return String.join(" ", scopes);
    }

}
