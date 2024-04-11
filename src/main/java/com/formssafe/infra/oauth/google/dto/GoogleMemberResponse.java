package com.formssafe.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.user.entity.Authority;
import com.formssafe.domain.user.entity.OauthId;
import com.formssafe.domain.user.entity.User;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String sub,
        String name,
        String givenName,
        String familyName,
        String picture,
        String email,
        Boolean emailVerified,
        String locale
) {

    public User toEntity(String refreshToken) {
        return User.builder()
                .oauthId(new OauthId(sub, OauthServerType.GOOGLE))
                .nickname(name)
                .email(email)
                .imageUrl(picture)
                .authority(Authority.ROLE_USER)
                .refreshToken(refreshToken)
                .isDeleted(false)
                .build();
    }
}