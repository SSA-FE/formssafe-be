package com.formssafe.domain.form.service;

import com.formssafe.domain.content.dto.ContentResponseDto;
import com.formssafe.domain.content.entity.Content;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormResponse.FormDetailDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.dto.RewardResponse.RewardListDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.reward.service.RewardRecipientsSelectService;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.dto.UserResponse.UserAuthorDto;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.exception.type.DataNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private final FormCommonService formCommonService;
    private final FormValidateService formValidateService;
    private final RewardRecipientsSelectService rewardRecipientsSelectService;
    private final FormDoneService formDoneService;
    private final FormRepository formRepository;

    public List<FormListDto> getList(SearchDto searchDto) {
        log.info(searchDto.toString());

        List<Form> result = formRepository.findFormWithFiltered(searchDto);
        return result.stream()
                .map(FormListDto::from)
                .toList();
    }

    public FormDetailDto getFormDetail(Long formId, LoginUserDto loginUser) {
        Form form = formCommonService.findForm(formId);
        formValidateService.validAuthorAndTemp(form, loginUser.id());

        UserAuthorDto userAuthorDto = getAuthor(form);
        List<TagListDto> tagListDtos = getTagList(form);
        List<ContentResponseDto> contentDetailDtos = getContentList(form);
        RewardListDto rewardDto = getReward(form);
        List<UserListDto> rewardRecipientsDtos = getRewardRecipients(rewardDto, form);

        return FormDetailDto.from(form,
                userAuthorDto,
                contentDetailDtos,
                rewardDto,
                tagListDtos,
                rewardRecipientsDtos);
    }

    public Form getForm(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id + "번 설문이 존재하지 않습니다."));

        if (form.isDeleted()) {
            throw new DataNotFoundException(id + "번 설문이 존재하지 않습니다.");
        }

        return form;
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

    private UserAuthorDto getAuthor(Form form) {
        User author = form.getUser();
        return UserAuthorDto.from(author);
    }

    private List<TagListDto> getTagList(Form form) {
        return form.getFormTagList().stream()
                .map(FormTag::getTag)
                .map(TagListDto::from)
                .toList();
    }

    private RewardListDto getReward(Form form) {
        Reward reward = form.getReward();
        if (reward == null) {
            return null;
        }

        return RewardListDto.from(reward, reward.getRewardCategory());
    }

    private List<UserListDto> getRewardRecipients(RewardListDto rewardDto, Form form) {
        List<UserListDto> rewardRecipientsDtos = Collections.emptyList();
        if (rewardDto != null) {
            rewardRecipientsDtos = getRewardRecipientList(form);
        }
        return rewardRecipientsDtos;
    }

    private List<ContentResponseDto> getContentList(Form form) {
        List<Content> contents = new ArrayList<>();
        contents.addAll(form.getDescriptiveQuestionList());
        contents.addAll(form.getObjectiveQuestionList());
        contents.addAll(form.getDecorationList());
        contents.sort(Comparator.comparingInt(Content::getPosition));

        return contents.stream()
                .map(ContentResponseDto::from)
                .toList();
    }

    private List<UserListDto> getRewardRecipientList(Form form) {
        if (FormStatus.REWARDED != form.getStatus()) {
            return Collections.emptyList();
        }

        return form.getRewardRecipientList().stream()
                .map(RewardRecipient::getUser)
                .map(UserListDto::from)
                .toList();
    }

    @Transactional
    public void deleteForm(Long id, LoginUserDto loginUser) {
        log.debug("Form delete: id: {}, loginUser: {}", id, loginUser.id());

        Form form = formCommonService.findForm(id);
        formValidateService.validAuthor(form, loginUser.id());

        form.delete();
    }

    @Transactional
    public void closeForm(Long id, LoginUserDto loginUser) {
        log.debug("Form close: id: {}, loginUser: {}", id, loginUser.id());

        Form form = formDoneService.execute(id, loginUser.id());
        if (form.getReward() != null) {
            rewardRecipientsSelectService.execute(form);
        }
    }
}
