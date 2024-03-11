package com.formssafe.domain.user.dto;

import com.formssafe.domain.user.entity.Authority;
import com.formssafe.domain.user.entity.User;

public record LoginUser(Long id,
                        String nickname,
                        String email,
                        Authority authority) {

    public static LoginUser fromEntity(User user) {
        return new LoginUser(user.id(), user.nickname(), user.email(), user.authority());
    }
}
