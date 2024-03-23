package com.formssafe.domain.form.service;

import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormResponse.FormDetailDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.question.dto.QuestionResponse.QuestionDetailDto;
import com.formssafe.domain.question.entity.DescriptiveQuestion;
import com.formssafe.domain.question.entity.ObjectiveQuestion;
import com.formssafe.domain.question.repository.DescriptiveQuestionRepository;
import com.formssafe.domain.question.repository.ObjectiveQuestionRepository;
import com.formssafe.domain.reward.dto.RewardResponse.RewardListDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.reward.repository.RewardRepository;
import com.formssafe.domain.tag.dto.TagResponse.TagCountDto;
import com.formssafe.domain.tag.dto.TagResponse.TagListDto;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.repository.FormTagRepository;
import com.formssafe.domain.tag.repository.TagRepository;
import com.formssafe.domain.user.dto.LoginUser;
import com.formssafe.domain.user.dto.UserResponse.UserAuthorDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private final ObjectiveQuestionRepository objectiveQuestionRepository;
    private final DescriptiveQuestionRepository descriptiveQuestionRepository;
    private final FormTagRepository formTagRepository;
    private final TagRepository tagRepository;
    private final RewardCategoryRepository rewardCategoryRepository;
    private final RewardRepository rewardRepository;

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

    public FormDetailDto get(LoginUser loginUser, Integer id) {
        log.debug("logined: {}", loginUser);

        User user = userRepository.findById(loginUser.id())
                .orElseGet(() -> User.builder()
                        .id(0L)
                        .nickname("탈퇴한 사용자")
                        .build()
                );
        UserAuthorDto userAuthorDto = UserAuthorDto.from(user);

        Form form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + "번 설문이 존재하지 않습니다."));

        List<TagListDto> tagListDto = form.getFormTagList().stream()
                .map(FormTag::getTag)
                .map(TagListDto::from)
                .toList();

        Reward reward = form.getReward();
        RewardListDto rewardListDto = null;
        if (reward != null) {
            rewardListDto = RewardListDto.from(reward, reward.getRewardCategory());
        }

        List<DescriptiveQuestion> descriptiveQuestions = form.getDescriptiveQuestions();
        List<ObjectiveQuestion> objectiveQuestions = form.getObjectiveQuestions();

        List<QuestionDetailDto> questions = new ArrayList<>(descriptiveQuestions.stream()
                .map(QuestionDetailDto::from)
                .toList());
        questions.addAll(objectiveQuestions.stream()
                .map(QuestionDetailDto::from)
                .toList());

        return FormDetailDto.from(form,
                userAuthorDto,
                questions,
                rewardListDto,
                tagListDto,
                Collections.emptyList());
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
