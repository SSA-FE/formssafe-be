package com.formssafe.domain.member.dto;

import com.formssafe.domain.member.entity.Authority;
import com.formssafe.domain.member.entity.Member;

public record LoginMember(Long id,
                          String nickname,
                          String email,
                          Authority authority) {

    public static LoginMember fromEntity(Member member) {
        return new LoginMember(member.id(), member.nickname(), member.email(), member.authority());
    }
}
