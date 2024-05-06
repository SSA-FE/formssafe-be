package com.formssafe.domain.form.service;

import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.content.service.ContentService;
import com.formssafe.domain.form.dto.FormRequest.FormUpdateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.notification.dto.NotificationEventDto.RewardCategoryRegistNotificationEventDto;
import com.formssafe.domain.notification.event.type.RewardCategoryRegistNotificationEvent;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.service.RewardService;
import com.formssafe.domain.subscribe.entity.Subscribe;
import com.formssafe.domain.subscribe.service.SubscribeService;
import com.formssafe.domain.tag.service.TagService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.util.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TempFormUpdateService {
    private final FormReadService formReadService;
    private final FormValidateService formValidateService;
    private final TagService tagService;
    private final ContentService contentService;
    private final RewardService rewardService;
    private final SubscribeService subscribeService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void execute(Long formId, FormUpdateDto request, LoginUserDto loginUser) {
        log.debug("TempFormUpdateService.execute: \nid {}\n loginUser {}", formId, loginUser);

        Form form = formReadService.findForm(formId);
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
        formValidateService.validTitle(request.title());
        formValidateService.validDescription(request.description());
        formValidateService.validImageSize(request.image());
        formValidateService.validTempFormExpectTime(request.expectTime());
        formValidateService.validExpectEndDate(now, request.endDate());
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
        formValidateService.validTitle(request.title());
        formValidateService.validDescription(request.description());
        formValidateService.validImageSize(request.image());
        formValidateService.validFormExpectTime(request.expectTime());
        formValidateService.validExpectEndDate(startDate, request.endDate());
        formValidateService.validPrivacyDisposalDate(request.privacyDisposalDate(), request.endDate());
        formValidateService.validQuestionCnt(questionCnt);

        clearFormRelatedData(form);
        form.updateToForm(request, startDate, request.endDate(), questionCnt);
        createFormRelatedData(request, form);

        if (form.getReward() != null) {
            publishRewardCategoryEvent(request.reward().category(), form, form.getUser());
        }
    }

    private int getQuestionCnt(List<ContentCreateDto> questions) {
        return (int) questions.stream()
                .filter(q -> !DecorationType.exists(q.type()))
                .count();
    }

    private void publishRewardCategoryEvent(String rewardCategoryName, Form form, User user) {
        RewardCategory rewardCategory = rewardService.getRewardCategoryFromRewardCategoryName(rewardCategoryName);
        List<Subscribe> subscribeList = subscribeService.getSubscribeUserByRewardCategory(
                rewardCategory.getId(), user);
        applicationEventPublisher.publishEvent(new RewardCategoryRegistNotificationEvent(
                new RewardCategoryRegistNotificationEventDto(form, subscribeList), this));
    }
}
