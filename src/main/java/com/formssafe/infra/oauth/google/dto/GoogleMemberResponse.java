package com.formssafe.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.formssafe.domain.member.entity.Authority;
import com.formssafe.domain.member.entity.Member;
import com.formssafe.domain.member.entity.OauthId;
import com.formssafe.domain.oauth.OauthServerType;
import java.time.LocalDateTime;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleMemberResponse(String sub,
                                   String name,
                                   String givenName,
                                   String familyName,
                                   String picture,
                                   String email,
                                   Boolean emailVerified,
                                   String locale) {

    public Member toEntity() {
        return Member.builder()
                .oauthId(new OauthId(sub, OauthServerType.GOOGLE))
                .nickname(name)
                .email(email)
                .imageUrl(picture)
                .authority(Authority.ROLE_USER)
                .createTime(LocalDateTime.now())
                .build();
    }
}