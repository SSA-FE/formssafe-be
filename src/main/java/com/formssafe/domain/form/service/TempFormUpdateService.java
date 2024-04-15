package com.formssafe.domain.form.service;

import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.content.service.ContentService;
import com.formssafe.domain.form.dto.FormRequest.FormUpdateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.reward.service.RewardService;
import com.formssafe.domain.tag.service.TagService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.global.util.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TempFormUpdateService {
    private final FormCommonService formCommonService;
    private final FormValidateService formValidateService;
    private final TagService tagService;
    private final ContentService contentService;
    private final RewardService rewardService;

    @Transactional
    public void execute(Long formId, FormUpdateDto request, LoginUserDto loginUser) {
        log.debug("TempFormUpdateService.execute: \nrequest {}\n loginUser {}");

        Form form = formCommonService.findForm(formId);
        formValidateService.validAuthor(form, loginUser.id());
        formValidateService.validTempForm(form);

        LocalDateTime now = DateTimeUtil.getCurrentDateTime();
        log.debug("now: {}, endDate: {}", now, request.endDate());

        int questionCnt = getQuestionCnt(request.contents());

        if (request.isTemp()) {
            updateToTempForm(request, form, now, questionCnt);
        } else {
            updateToForm(request, form, now, questionCnt);
        }
    }

    private void updateToTempForm(FormUpdateDto request, Form form, LocalDateTime now, int questionCnt) {
        formValidateService.validAutoEndDate(now, request.endDate());
        formValidateService.validPrivacyDisposalDate(request.privacyDisposalDate(), request.endDate());

        clearFormRelatedData(form);
        form.updateToTempForm(request, request.endDate(), questionCnt);
        createFormRelatedData(request, form);
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

    private void updateToForm(FormUpdateDto request, Form form, LocalDateTime startDate, int questionCnt) {
        formValidateService.validAutoEndDate(startDate, request.endDate());
        formValidateService.validPrivacyDisposalDate(request.privacyDisposalDate(), request.endDate());
        formValidateService.validQuestionCnt(questionCnt);

        clearFormRelatedData(form);
        form.updateToForm(request, startDate, request.endDate(), questionCnt);
        createFormRelatedData(request, form);
    }

    private int getQuestionCnt(List<ContentCreateDto> questions) {
        return (int) questions.stream()
                .filter(q -> !DecorationType.exists(q.type()))
                .count();
    }
}
