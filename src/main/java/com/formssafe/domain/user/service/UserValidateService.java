package com.formssafe.domain.user.service;

import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class UserValidateService {
    public void validUserNickname(String nickname) {
        if (nickname.length() < 1 || nickname.length() > 20) {
            throw new BadRequestException(ErrorCode.INVALID_USER_NICKNAME,
                    "사용자의 nickname은 1자 이상 20자 이하를 만족해야 합니다 : " + nickname);
        }
    }
}
