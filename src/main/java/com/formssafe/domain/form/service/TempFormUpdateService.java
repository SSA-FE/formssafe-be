package com.formssafe.domain.form.service;

import com.formssafe.domain.batch.form.service.FormBatchService;
import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.content.service.ContentService;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormRequest.FormUpdateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.service.RewardService;
import com.formssafe.domain.tag.service.TagService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import com.formssafe.global.exception.type.ForbiddenException;
import com.formssafe.global.util.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TempFormUpdateService {
    private final FormService formService;
    private final TagService tagService;
    private final ContentService contentService;
    private final RewardService rewardService;
    private final FormBatchService formBatchService;
    private final UserRepository userRepository;

    @Transactional
    public void execute(Long formId, FormUpdateDto request, LoginUserDto loginUser) {
        log.debug("TempFormUpdateService.execute: \nrequest {}\n loginUser {}");

        Form form = formService.findForm(formId);

        formService.validAuthor(form, loginUser.id());
        formService.validTempForm(form);

        LocalDateTime now = DateTimeUtil.getCurrentDateTime();
        LocalDateTime endDate =
                request.endDate() == null ? null : DateTimeUtil.truncateSecondsAndNanos(request.endDate());
        log.debug("now: {}, endDate: {}", now, endDate);

        int questionCnt = getQuestionCnt(request.contents());

        if (request.isTemp()) {
            updateToTempForm(request, form, now, endDate, questionCnt);
        } else {
            updateToForm(request, form, now, endDate, questionCnt);
        }
    }

    private void updateToTempForm(FormUpdateDto request, Form form, LocalDateTime now, LocalDateTime endDate,
                                  int questionCnt) {
        validateTempForm(now, endDate, request.privacyDisposalDate());

        clearFormRelatedData(form);
        form.updateToTempForm(request, endDate, questionCnt);
        createFormRelatedData(request, form);
    }

    private void validateTempForm(LocalDateTime now, LocalDateTime endDate, LocalDateTime privacyDisposalDate) {
        if (endDate != null) {
            if (!now.plusMinutes(5L).isBefore(endDate)) {
                throw new BadRequestException("자동 마감 시각은 현재 시각 5분 후부터 설정할 수 있습니다.: " + endDate);
            }

            if (privacyDisposalDate != null && privacyDisposalDate.isBefore(endDate)) {
                throw new BadRequestException("개인 정보 폐기 시각은 마감 시각 후여야 합니다.");
            }
        }
    }

    private void clearFormRelatedData(Form form) {
        contentService.deleteContents(form);
        tagService.decreaseCount(form);
        if (form.getReward() != null) {
            rewardService.deleteReward(form);
        }
    }

    private void createFormRelatedData(FormUpdateDto request, Form form) {
        contentService.createContents(request.contents(), form);
        tagService.createOrUpdateTags(request.tags(), form);
        if (request.reward() != null) {
            rewardService.createReward(request.reward(), form);
        }
    }

    private void updateToForm(FormUpdateDto request, Form form, LocalDateTime startDate, LocalDateTime endDate,
                              int questionCnt) {
        validateForm(startDate, endDate, request.privacyDisposalDate(), questionCnt);

        clearFormRelatedData(form);
        form.updateToForm(request, startDate, endDate, questionCnt);
        createFormRelatedData(request, form);
        registerFormEndBatch(endDate, form);
    }

    private int getQuestionCnt(List<ContentCreateDto> questions) {
        return (int) questions.stream()
                .filter(q -> !DecorationType.exists(q.type()))
                .count();
    }

    private void validateForm(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime privacyDisposalDate,
                              int questionCnt) {
        if (endDate != null && !startDate.plusMinutes(5L).isBefore(endDate)) {
            throw new BadRequestException("자동 마감 시각은 현재 시각 5분 후부터 설정할 수 있습니다.: " + endDate);
        }

        if (endDate != null && privacyDisposalDate != null &&
                privacyDisposalDate.isBefore(endDate)) {
            throw new BadRequestException("개인 정보 폐기 시각은 마감 시각 후여야 합니다.");
        }

        if (questionCnt == 0) {
            throw new BadRequestException("설문에는 하나 이상의 설문 문항이 포함되어야 합니다.");
        }
    }

    private void registerFormEndBatch(LocalDateTime endDate, Form form) {
        if (endDate != null) {
            formBatchService.registerEndForm(endDate, form);
        }
    }
}
