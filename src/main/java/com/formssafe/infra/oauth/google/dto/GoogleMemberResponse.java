package com.formssafe.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.user.entity.Authority;
import com.formssafe.domain.user.entity.OauthId;
import com.formssafe.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonNaming(SnakeCaseStrategy.class)
@Setter
@Getter
public class GoogleMemberResponse {
    private String sub;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String email;
    private Boolean emailVerified;
    private String locale;
    private String refreshToken;
    public GoogleMemberResponse(){}

    public GoogleMemberResponse(String sub, String name, String givenName, String familyName, String picture, String email, Boolean emailVerified, String locale) {
        this.sub = sub;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.picture = picture;
        this.email = email;
        this.emailVerified = emailVerified;
        this.locale = locale;
        this.refreshToken = null;
    }

    public User toEntity() {
        return User.builder()
                .oauthId(new OauthId(sub, OauthServerType.GOOGLE))
                .nickname(name)
                .email(email)
                .imageUrl(picture)
                .authority(Authority.ROLE_USER)
                .createTime(LocalDateTime.now())
                .refreshToken(refreshToken)
                .build();
    }
}