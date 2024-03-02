package com.formssafe.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.entity.OauthId;
import com.formssafe.domain.oauth.entity.OauthMember;
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

    public OauthMember toEntity() {
        return OauthMember.builder()
                .oauthId(new OauthId(sub, OauthServerType.GOOGLE, email))
                .nickname(name)
                .imageUrl(picture)
                .createTime(LocalDateTime.now())
                .build();
    }
}