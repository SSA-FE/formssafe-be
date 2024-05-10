package com.formssafe.domain.form.service;

import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.form.dto.FormResponse.FormResultDto;
import com.formssafe.domain.form.dto.FormResponse.FormWithQuestionDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.reward.service.RewardRecipientsSelectService;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormService {
    private final FormReadService formReadService;
    private final FormValidateService formValidateService;
    private final RewardRecipientsSelectService rewardRecipientsSelectService;
    private final FormFinishService formFinishService;
    private final FormRepository formRepository;
    private final FormResponseMapper formResponseMapper;

    public FormWithQuestionDto getForm(Long formId, LoginUserDto loginUser) {
        Form form = formReadService.findFormWithUserAndTag(formId);
        formValidateService.validAuthor(form, loginUser.id());
        formValidateService.validTempForm(form);

        List<Content> contents = formReadService.getContentList(formId);
        List<FormTag> formTags = form.getFormTagList();
        Reward reward = form.getReward();

        return FormWithQuestionDto.from(form,
                formResponseMapper.toContentResponseDto(contents),
                formResponseMapper.toTagListDto(formTags),
                formResponseMapper.toRewardDto(reward));
    }

    public FormResultDto getFormResult(Long formId, LoginUserDto loginUser) {
        Form form = formReadService.findFormWithUserAndTag(formId);
        formValidateService.validAuthor(form, loginUser.id());
        formValidateService.validNotTempForm(form);

        List<Content> contents = formReadService.getContentList(formId);
        List<FormTag> formTags = form.getFormTagList();
        Reward reward = form.getReward();
        List<RewardRecipient> rewardRecipients = null;
        if (reward != null) {
            rewardRecipients = form.getRewardRecipientList();
        }

        return FormResultDto.from(form,
                formResponseMapper.toContentResponseDto(contents),
                formResponseMapper.toTagListDto(formTags),
                formResponseMapper.toRewardDto(reward),
                formResponseMapper.toRewardRecipientsListDto(rewardRecipients));
    }

    public Form getForm(Long id) {
        return formReadService.findForm(id);
    }

    public int getRequiredQuestionCnt(Form form) {
        int requiredQuestionCnt = 0;
        for (DescriptiveQuestion question : form.getDescriptiveQuestionList()) {
            if (question.isRequired()) {
                requiredQuestionCnt++;
            }
        }
        for (ObjectiveQuestion question : form.getObjectiveQuestionList()) {
            if (question.isRequired()) {
                requiredQuestionCnt++;
            }
        }
        return requiredQuestionCnt;
    }

    public void deleteFormByUser(User user) {
        formRepository.deleteFormByUserId(user.getId());
    }

    @Transactional
    public void deleteForm(Long id, LoginUserDto loginUser) {
        log.debug("Form delete: id: {}, loginUser: {}", id, loginUser.id());

        Form form = formReadService.findForm(id);
        formValidateService.validAuthor(form, loginUser.id());

        form.delete();
    }

    @Transactional
    public void closeForm(Long id, LoginUserDto loginUser) {
        log.debug("Form close: id: {}, loginUser: {}", id, loginUser.id());

        Form form = formFinishService.execute(id, loginUser.id());
        if (form.getReward() != null) {
            rewardRecipientsSelectService.execute(form);
        }
    }
}