package com.formssafe.domain.form.service;

import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.content.service.ContentService;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormResponse.FormIdDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.notification.dto.NotificationEventDto.RewardCategoryRegistNotificationEventDto;
import com.formssafe.domain.notification.event.type.RewardCategoryRegistNotificationEvent;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.service.RewardService;
import com.formssafe.domain.subscribe.entity.Subscribe;
import com.formssafe.domain.subscribe.service.SubscribeService;
import com.formssafe.domain.tag.service.TagService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
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
public class FormCreateService {
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final FormValidateService formValidateService;
    private final TagService tagService;
    private final ContentService contentService;
    private final RewardService rewardService;
    private final SubscribeService subscribeService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FormResponseMapper formResponseMapper;

    @Transactional
    public FormIdDto execute(FormCreateDto request, LoginUserDto loginUser) {
        log.debug("FormCreateService.execute: \nrequest {}\n loginUser {}", request, loginUser);

        User user = userRepository.getReferenceById(loginUser.id());
        LocalDateTime now = DateTimeUtil.getCurrentDateTime();
        int questionCnt = getQuestionCnt(request.contents());
        log.debug("now: {}, endDate: {}, questionCnt: {}", now, request.endDate(), questionCnt);

        Long formId = null;
        if (request.isTemp()) {
            formId = createTempForm(request, user, now, questionCnt);
        } else {
            formId = createForm(request, user, now, questionCnt);
        }

        return formResponseMapper.toFormIdDto(formId);
    }

    private int getQuestionCnt(List<ContentCreateDto> questions) {
        return (int) questions.stream()
                .filter(q -> !DecorationType.exists(q.type()))
                .count();
    }

    private Long createTempForm(FormCreateDto request, User user, LocalDateTime now, int questionCnt) {
        formValidateService.validTitle(request.title());
        formValidateService.validDescription(request.description());
        formValidateService.validImageSize(request.image());
        formValidateService.validTempFormExpectTime(request.expectTime());
        formValidateService.validExpectEndDate(now, request.endDate());
        formValidateService.validPrivacyDisposalDate(request.privacyDisposalDate(), request.endDate());

        Form form = Form.createTempForm(request, user, request.endDate(), questionCnt);
        formRepository.save(form);

        createFormRelatedData(request, form);

        return form.getId();
    }

    private void createFormRelatedData(FormCreateDto request, Form form) {
        contentService.createContents(request.contents(), form);
        tagService.createOrUpdateTags(request.tags(), form);
        if (request.reward() != null) {
            rewardService.createReward(request.reward(), form);
        }
    }

    private Long createForm(FormCreateDto request, User user, LocalDateTime startDate, int questionCnt) {
        formValidateService.validTitle(request.title());
        formValidateService.validDescription(request.description());
        formValidateService.validImageSize(request.image());
        formValidateService.validFormExpectTime(request.expectTime());
        formValidateService.validExpectEndDate(startDate, request.endDate());
        formValidateService.validPrivacyDisposalDate(request.privacyDisposalDate(), request.endDate());
        formValidateService.validQuestionCnt(questionCnt);

        Form form = Form.createForm(request, user, startDate, request.endDate(), questionCnt);
        formRepository.save(form);

        createFormRelatedData(request, form);

        if (form.getReward() != null) {
            publishRewardCategoryEvent(request.reward().category(), form, user);
        }

        return form.getId();
    }

    private void publishRewardCategoryEvent(String rewardCategoryName, Form form, User user) {
        RewardCategory rewardCategory = rewardService.getRewardCategoryFromRewardCategoryName(rewardCategoryName);
        List<Subscribe> subscribeList = subscribeService.getSubscribeUserByRewardCategory(
                rewardCategory.getId(), user);
        applicationEventPublisher.publishEvent(new RewardCategoryRegistNotificationEvent(
                new RewardCategoryRegistNotificationEventDto(form, subscribeList), this));
    }
}
