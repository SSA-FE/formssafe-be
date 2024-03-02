package com.formssafe.infra.oauth.google.config;

import java.util.Arrays;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleOauthConfig(String redirectUri,
                                String clientId,
                                String clientSecret,
                                String[] scope) {

    @Override
    public String toString() {
        return "GoogleOauthConfig{" +
                "redirectUri='" + redirectUri + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", scope=" + Arrays.toString(scope) +
                '}';
    }
}
