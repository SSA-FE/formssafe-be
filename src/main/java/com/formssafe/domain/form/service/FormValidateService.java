package com.formssafe.domain.form.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import com.formssafe.global.exception.type.ForbiddenException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormValidateService {

    public void validAuthor(Form form, Long loginUserId) {
        if (!Objects.equals(form.getUser().getId(), loginUserId)) {
            throw new ForbiddenException("login user - " + loginUserId + ": 설문 작성자가 아닙니다.: " + form.getUser().getId());
        }
    }

    public void validTempForm(Form form) {
        if (!form.isTemp()) {
            throw new BadRequestException("임시 설문이 아닙니다: " + form.getId());
        }
    }

    public void validAuthorAndTemp(Form form, Long loginUserId) {
        if (!Objects.equals(form.getUser().getId(), loginUserId) && form.isTemp()) {
            throw new DataNotFoundException("해당 설문이 존재하지 않습니다.: " + form.getId());
        }
    }

    public void validFormProgress(Form form) {
        if (!FormStatus.PROGRESS.equals(form.getStatus())) {
            throw new BadRequestException("현재 진행 중인 설문이 아닙니다.");
        }
    }
}
