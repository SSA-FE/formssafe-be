package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createContentCreate;
import static org.assertj.core.api.Assertions.assertThat;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormRequest.FormUpdateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.util.DateTimeUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TempFormUpdateServiceTest extends IntegrationTestConfig {
    private final TempFormUpdateService tempFormUpdateService;
    private final FormCreateService formCreateService;
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final RewardCategoryRepository rewardCategoryRepository;
    private final EntityManager em;

    private User testUser;

    @Autowired
    public TempFormUpdateServiceTest(TempFormUpdateService tempFormUpdateService, FormCreateService formCreateService,
                                     UserRepository userRepository,
                                     FormRepository formRepository,
                                     RewardCategoryRepository rewardCategoryRepository,
                                     EntityManager em) {
        this.tempFormUpdateService = tempFormUpdateService;
        this.formCreateService = formCreateService;
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.rewardCategoryRepository = rewardCategoryRepository;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        rewardCategoryRepository.save(RewardCategory.builder().rewardCategoryName("커피").build());
        testUser = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
    }

    @Test
    void 임시설문을_설문으로_수정한다() {
        //given
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                null, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4), true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());

        formCreateService.execute(formCreateDto, loginUserDto);
        Form form = formRepository.findAll().get(0);

        em.flush();
        em.clear();

        LocalDateTime endDate = DateTimeUtil.truncateSecondsAndNanos(LocalDateTime.now().plusDays(1));
        FormUpdateDto formUpdate = new FormUpdateDto("업데이트1", "업데이트1", null,
                endDate, 5, true, null,
                List.of(createContentCreate("text", null, "텍스트 블록-업데이트", null, false),
                        createContentCreate("short", "주관식 질문-업데이트", null, null, false),
                        createContentCreate("checkbox", "객관식 질문-업데이트", null, List.of("1", "2", "3"), false),
                        createContentCreate("text", null, "텍스트 블록-추가", null, false),
                        createContentCreate("long", "주관식 질문-추가", null, null, false),
                        createContentCreate("single", "객관식 질문-추가", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tagNew"),
                new RewardCreateDto("경품1-업데이트", "커피", 5),
                false);
        //when
        tempFormUpdateService.execute(form.getId(), formUpdate, loginUserDto);
        //then
        em.flush();
        em.clear();

        List<Form> result = formRepository.findAll();
        assertThat(result).hasSize(1);
        Form resultForm = formRepository.findById(result.get(0).getId()).orElseThrow(IllegalStateException::new);

        assertThat(resultForm.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(resultForm.getStartDate()).isNotNull();
        assertThat(resultForm.getStatus()).isEqualTo(FormStatus.PROGRESS);
        assertThat(resultForm.getDecorationList()).hasSize(2)
                .extracting("detail", "position")
                .containsExactly(Tuple.tuple("텍스트 블록-업데이트", 1),
                        Tuple.tuple("텍스트 블록-추가", 4));
        assertThat(resultForm.getDescriptiveQuestionList()).hasSize(2)
                .extracting("title", "position")
                .containsExactly(Tuple.tuple("주관식 질문-업데이트", 2),
                        Tuple.tuple("주관식 질문-추가", 5));
        assertThat(resultForm.getObjectiveQuestionList()).hasSize(2)
                .extracting("title", "position")
                .containsExactly(Tuple.tuple("객관식 질문-업데이트", 3),
                        Tuple.tuple("객관식 질문-추가", 6));

        List<Tag> tagList = resultForm.getFormTagList().stream()
                .map(FormTag::getTag)
                .toList();
        assertThat(tagList).hasSize(2)
                .extracting("tagName", "count")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("tag1", 1),
                        Tuple.tuple("tagNew", 1));

        assertThat(resultForm.getReward().getRewardName()).isEqualTo("경품1-업데이트");
    }

    @Test
    void 임시설문을_새로운임시설문으로_수정한다() {
        //given
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                null, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4), true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());

        formCreateService.execute(formCreateDto, loginUserDto);
        Form form = formRepository.findAll().get(0);

        em.flush();
        em.clear();

        FormUpdateDto formUpdate = new FormUpdateDto("업데이트1", "업데이트1", null,
                null, 5, true, null,
                List.of(createContentCreate("text", null, "텍스트 블록-업데이트", null, false),
                        createContentCreate("short", "주관식 질문-업데이트", null, null, false),
                        createContentCreate("checkbox", "객관식 질문-업데이트", null, List.of("1", "2", "3"), false),
                        createContentCreate("text", null, "텍스트 블록-추가", null, false),
                        createContentCreate("long", "주관식 질문-추가", null, null, false),
                        createContentCreate("single", "객관식 질문-추가", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tagNew"),
                new RewardCreateDto("경품1-업데이트", "커피", 5),
                true);
        //when
        tempFormUpdateService.execute(form.getId(), formUpdate, loginUserDto);
        //then
        em.flush();
        em.clear();

        List<Form> result = formRepository.findAll();
        assertThat(result).hasSize(1);
        Form resultForm = formRepository.findById(result.get(0).getId()).orElseThrow(IllegalStateException::new);

        assertThat(resultForm.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(resultForm.getStartDate()).isNull();
        assertThat(resultForm.getStatus()).isEqualTo(FormStatus.NOT_STARTED);
        assertThat(resultForm.getDecorationList()).hasSize(2)
                .extracting("detail", "position")
                .containsExactly(Tuple.tuple("텍스트 블록-업데이트", 1),
                        Tuple.tuple("텍스트 블록-추가", 4));
        assertThat(resultForm.getDescriptiveQuestionList()).hasSize(2)
                .extracting("title", "position")
                .containsExactly(Tuple.tuple("주관식 질문-업데이트", 2),
                        Tuple.tuple("주관식 질문-추가", 5));
        assertThat(resultForm.getObjectiveQuestionList()).hasSize(2)
                .extracting("title", "position")
                .containsExactly(Tuple.tuple("객관식 질문-업데이트", 3),
                        Tuple.tuple("객관식 질문-추가", 6));

        List<Tag> tagList = resultForm.getFormTagList().stream()
                .map(FormTag::getTag)
                .toList();
        assertThat(tagList).hasSize(2)
                .extracting("tagName", "count")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("tag1", 1),
                        Tuple.tuple("tagNew", 1));

        assertThat(resultForm.getReward().getRewardName()).isEqualTo("경품1-업데이트");
    }
}