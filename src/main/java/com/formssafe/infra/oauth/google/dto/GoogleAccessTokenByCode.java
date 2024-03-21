package com.formssafe.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleAccessTokenByCode(String tokenType,
                                      String accessToken,
                                      Integer expiresIn,
                                      String refreshToken,
                                      String scope) {
}

