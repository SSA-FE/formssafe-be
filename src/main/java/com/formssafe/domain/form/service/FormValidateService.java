package com.formssafe.domain.form.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
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
            throw new ForbiddenException(ErrorCode.INVALID_FORM_AUTHOR,
                    "Invalid author id " + loginUserId + " for form author id " + form.getUser().getId());
        }
    }

    public void validTempForm(Form form) {
        if (!form.isTemp()) {
            throw new BadRequestException(ErrorCode.NOT_TEMP_FORM, "Not temp form id" + form.getId());
        }
    }

    public void validNotTempForm(Form form) {
        if (form.isTemp()) {
            throw new BadRequestException(ErrorCode.TEMP_FORM, "Temp form id " + form.getId());
        }
    }

    public void validFormProgress(Form form) {
        if (!FormStatus.PROGRESS.equals(form.getStatus())) {
            throw new BadRequestException(ErrorCode.NOT_PROGRESS_FORM,
                    "Not progress form id " + form.getId() + " status " + form.getStatus());
        }
    }

    public void validAutoEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate != null && !startDate.plusMinutes(5L).isBefore(endDate)) {
            throw new BadRequestException(ErrorCode.INVALID_AUTO_END_DATE,
                    "Invalid auto end date " + endDate + " for start date " + startDate);
        }
    }

    public void validPrivacyDisposalDate(LocalDateTime privacyDisposalDate, LocalDateTime endDate) {
        if (endDate != null && privacyDisposalDate != null &&
                privacyDisposalDate.isBefore(endDate)) {
            throw new BadRequestException(ErrorCode.INVALID_PRIVACY_DISPOSAL_DATE,
                    "Invalid privacy disposal date " + privacyDisposalDate + " for end date " + endDate);
        }
    }

    public void validQuestionCnt(int questionCnt) {
        if (questionCnt <= 0) {
            throw new BadRequestException(ErrorCode.EMPTY_QUESTION, "Empty question count " + questionCnt);
        }
    }
}
