package com.formssafe.domain.form.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.global.error.type.DataNotFoundException;
import com.formssafe.global.error.type.ForbiddenException;
import java.time.LocalDateTime;
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

    public void validNotTempForm(Form form) {
        if (form.isTemp()) {
            throw new BadRequestException("임시 설문입니다: " + form.getId());
        }
    }

    public void validAuthorAndTemp(Form form, Long loginUserId) {
        if (!Objects.equals(form.getUser().getId(), loginUserId) && form.isTemp()) {
            throw new DataNotFoundException(ErrorCode.FORM_NOT_FOUND,
                    "login user - " + loginUserId + ": 임시 설문 작성자가 아닙니다.: " + form.getUser().getId());
        }
    }

    public void validFormProgress(Form form) {
        if (!FormStatus.PROGRESS.equals(form.getStatus())) {
            throw new BadRequestException("현재 진행 중인 설문이 아닙니다.");
        }
    }

    public void validAutoEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate != null && !startDate.plusMinutes(5L).isBefore(endDate)) {
            throw new BadRequestException("자동 마감 시각은 현재 시각 5분 후부터 설정할 수 있습니다.: " + endDate);
        }
    }

    public void validPrivacyDisposalDate(LocalDateTime privacyDisposalDate, LocalDateTime endDate) {
        if (endDate != null && privacyDisposalDate != null &&
                privacyDisposalDate.isBefore(endDate)) {
            throw new BadRequestException("개인 정보 폐기 시각은 마감 시각과 같거나 후여야 합니다.");
        }
    }

    public void validQuestionCnt(int questionCnt) {
        if (questionCnt <= 0) {
            throw new BadRequestException("설문에는 하나 이상의 설문 문항이 포함되어야 합니다.");
        }
    }
}
