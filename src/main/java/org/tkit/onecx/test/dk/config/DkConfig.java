package org.tkit.onecx.test.dk.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "onecx.test.dk")
public interface DkConfig {

    /**
     * OIDC configuration.
     */
    @WithName("oidc")
    OidcConfig oidc();

    /**
     * Store configuration.
     */
    @WithName("store")
    StoreConfig store();

    /**
     * OIDC configuration.
     */
    interface OidcConfig {

        /**
         * Issuer configuration.
         */
        @WithName("issuer")
        String issuer();

        /**
         * Token lifetime.
         */
        @WithName("token-lifetime")
        @WithDefault("3600")
        long tokenLifetime();

        /**
         * Token skew.
         */
        @WithName("token-skew")
        @WithDefault("30")
        int tokenSkew();

        /**
         * Refresh token lifetime.
         */
        @WithName("refresh-token-lifetime")
        @WithDefault("2592000")
        long refreshLifetime();
    }

    interface StoreConfig {

        /**
         * Store directory.
         */
        @WithName("directory")
        Optional<String> directory();
    }
}
