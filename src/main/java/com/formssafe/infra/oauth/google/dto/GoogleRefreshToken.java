package com.formssafe.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleRefreshToken(String tokenType,
                                String accessToken,
                                Integer expiresIn,
                                String scope) {
}