package com.formssafe.domain.form.service;

import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormResponse.FormDetailDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.question.dto.QuestionResponse.QuestionDetailDto;
import com.formssafe.domain.question.entity.Question;
import com.formssafe.domain.reward.dto.RewardResponse.RewardListDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.tag.entity.FormTag;
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
    private final FormRepository formRepository;

    public List<FormListDto> getList(SearchDto searchDto) {
        log.info(searchDto.toString());
        List<Form> result = formRepository.findFormAllFiltered(searchDto);
        return result.stream()
                .map(FormListDto::from)
                .toList();
    }

    public FormDetailDto getFormDetail(Long id) {
        Form form = getForm(id);
        UserAuthorDto userAuthorDto = getAuthor(form);
        List<TagListDto> tagListDtos = getTagList(form);

        List<QuestionDetailDto> questionDetailDtos = getQuestionList(form);

        List<UserListDto> rewardRecipientsDtos = Collections.emptyList();
        RewardListDto rewardDto = getReward(form);
        if (rewardDto != null) {
            rewardRecipientsDtos = getRewardRecipientList(form);
        }

        return FormDetailDto.from(form,
                userAuthorDto,
                questionDetailDtos,
                rewardDto,
                tagListDtos,
                rewardRecipientsDtos);
    }

    private UserAuthorDto getAuthor(Form form) {
        User author = form.getUser();
        return UserAuthorDto.from(author);
    }

    private Form getForm(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(id + "번 설문이 존재하지 않습니다."));

        if (form.isDeleted()) {
            throw new DataNotFoundException(id + "번 설문이 존재하지 않습니다.");
        }

        return form;
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

    private List<QuestionDetailDto> getQuestionList(Form form) {
        List<Question> questions = new ArrayList<>();
        questions.addAll(form.getDescriptiveQuestionList());
        questions.addAll(form.getObjectiveQuestionList());
        questions.sort(Comparator.comparingInt(Question::getPosition));

        return questions.stream()
                .map(QuestionDetailDto::from)
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

    public void create(FormCreateDto request) {
        log.debug(request.toString());
    }

    public void update(Long id, FormCreateDto request) {
        log.debug("id: {}\n payload: {}", id, request.toString());
    }

    public void delete(Long id) {
        log.debug("id: {}", id);
    }

    public void close(Long id) {
        log.debug("id: {}", id);
    }
}
