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
import com.formssafe.domain.tag.dto.TagResponse.TagCountDto;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.user.dto.UserResponse.UserAuthorDto;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FormService {
    private final FormRepository formRepository;
    private final UserRepository userRepository;

    public Page<FormListDto> getList(SearchDto params) {
        log.debug(params.toString());

        FormListDto formListResponse1Dto = new FormListDto(1, "title1", "thumbnail1",
                new UserAuthorDto(1L, "minji"), 10, 2, 2,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardListDto("냉장고", "가전제품", 3),
                new TagCountDto[]{new TagCountDto(1, "tag1", 3),
                        new TagCountDto(2, "tag2", 3)},
                FormStatus.PROGRESS.displayName());

        FormListDto formListResponse2Dto = new FormListDto(1, "title2", "thumbnail2",
                new UserAuthorDto(2L, "hyukjin"), 5, 3, 3,
                LocalDateTime.of(2024, 2, 29, 0, 0), LocalDateTime.of(2024, 3, 1, 0, 0),
                new RewardListDto("청소기", "가전제품", 2),
                new TagCountDto[]{new TagCountDto(2, "tag2", 3),
                        new TagCountDto(4, "tag4", 3)},
                FormStatus.DONE.displayName());

        return new PageImpl<>(List.of(formListResponse1Dto, formListResponse2Dto));
    }

    public FormDetailDto get(Integer id) {
        Form form = getForm(id);
        UserAuthorDto userAuthorDto = getAuthor(form);
        List<TagListDto> tagListDtos = getTagList(form);
        RewardListDto rewardListDto = getReward(form);
        List<QuestionDetailDto> questionDetailDtos = getQuestionList(form);
        List<UserListDto> userListDtos = getRewardRecipientList(form);

        return FormDetailDto.from(form,
                userAuthorDto,
                questionDetailDtos,
                rewardListDto,
                tagListDtos,
                userListDtos);
    }

    private UserAuthorDto getAuthor(Form form) {
        User author = form.getUser();
        return UserAuthorDto.from(author);
    }

    private Form getForm(Integer id) {
        return formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + "번 설문이 존재하지 않습니다."));
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
