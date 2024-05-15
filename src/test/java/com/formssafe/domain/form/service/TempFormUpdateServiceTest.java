package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createContentCreate;
import static com.formssafe.util.Fixture.createDescriptiveQuestion;
import static com.formssafe.util.Fixture.createFormTag;
import static com.formssafe.util.Fixture.createReward;
import static com.formssafe.util.Fixture.createTag;
import static com.formssafe.util.Fixture.createTemporaryForm;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.form.dto.FormRequest.FormUpdateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.util.DateTimeUtil;
import com.formssafe.util.AssertionUtil;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("[설문 수정 비즈니스 레이어 테스트]")
class TempFormUpdateServiceTest extends IntegrationTestConfig {
    private final TempFormUpdateService tempFormUpdateService;
    private final EntityManager em;

    private User testUser;
    private RewardCategory rewardCategory;

    @Autowired
    public TempFormUpdateServiceTest(TempFormUpdateService tempFormUpdateService,
                                     EntityManager em) {
        this.tempFormUpdateService = tempFormUpdateService;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        rewardCategory = RewardCategory.builder()
                .rewardCategoryName("test")
                .build();
        em.persist(rewardCategory);
        testUser = em.find(User.class, 1L);
    }

    @Test
    void 임시_설문을_설문으로_수정한다() {
        //given
        Form form = createTemporaryForm(testUser, "제목1", "설명1");
        DescriptiveQuestion dq1 = createDescriptiveQuestion(form, DescriptiveQuestionType.SHORT, "주관식 질문", 1);
        DescriptiveQuestion dq2 = createDescriptiveQuestion(form, DescriptiveQuestionType.SHORT, "주관식 질문", 2);
        Tag tag1 = createTag("tag1");
        Tag tag13 = createTag("tag13");
        FormTag formTag1 = createFormTag(form, tag1);
        FormTag formTag2 = createFormTag(form, tag13);
        Reward reward = createReward("경품1", form, rewardCategory, 4);
        em.persist(form);
        em.persist(dq1);
        em.persist(dq2);
        em.persist(tag1);
        em.persist(tag13);
        em.persist(formTag1);
        em.persist(formTag2);
        em.persist(reward);
        EntityManagerUtil.flushAndClear(em);

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
                new RewardCreateDto("경품1-업데이트", rewardCategory.getRewardCategoryName(), 5),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when
        tempFormUpdateService.execute(form.getId(), formUpdate, loginUserDto);
        //then
        EntityManagerUtil.flushAndClear(em);

        Form resultForm = em.find(Form.class, form.getId());
        AssertionUtil.assertWithSoftAssertions(s -> {
            s.assertThat(resultForm.getUser().getId()).isEqualTo(testUser.getId());
            s.assertThat(resultForm.getStartDate()).isNotNull();
            s.assertThat(resultForm.getStatus()).isEqualTo(FormStatus.PROGRESS);
            s.assertThat(resultForm.getDecorationList()).hasSize(2)
                    .extracting("detail", "position")
                    .containsExactly(Tuple.tuple("텍스트 블록-업데이트", 1),
                            Tuple.tuple("텍스트 블록-추가", 4));
            s.assertThat(resultForm.getDescriptiveQuestionList()).hasSize(2)
                    .extracting("title", "position")
                    .containsExactly(Tuple.tuple("주관식 질문-업데이트", 2),
                            Tuple.tuple("주관식 질문-추가", 5));
            s.assertThat(resultForm.getObjectiveQuestionList()).hasSize(2)
                    .extracting("title", "position")
                    .containsExactly(Tuple.tuple("객관식 질문-업데이트", 3),
                            Tuple.tuple("객관식 질문-추가", 6));

            List<Tag> tagList = resultForm.getFormTagList().stream()
                    .map(FormTag::getTag)
                    .toList();
            s.assertThat(tagList).hasSize(2)
                    .extracting("tagName", "count")
                    .containsExactlyInAnyOrder(
                            Tuple.tuple("tag1", 1),
                            Tuple.tuple("tagNew", 1));

            s.assertThat(resultForm.getReward().getRewardName()).isEqualTo("경품1-업데이트");
        });
    }

    @Test
    void 임시_설문을_임시_설문으로_수정한다() {
        //given
        Form form = createTemporaryForm(testUser, "제목1", "설명1");
        DescriptiveQuestion dq1 = createDescriptiveQuestion(form, DescriptiveQuestionType.SHORT, "주관식 질문", 1);
        DescriptiveQuestion dq2 = createDescriptiveQuestion(form, DescriptiveQuestionType.SHORT, "주관식 질문", 2);
        Tag tag1 = createTag("tag1");
        Tag tag13 = createTag("tag13");
        FormTag formTag1 = createFormTag(form, tag1);
        FormTag formTag2 = createFormTag(form, tag13);
        Reward reward = createReward("경품1", form, rewardCategory, 4);
        em.persist(form);
        em.persist(dq1);
        em.persist(dq2);
        em.persist(tag1);
        em.persist(tag13);
        em.persist(formTag1);
        em.persist(formTag2);
        em.persist(reward);
        EntityManagerUtil.flushAndClear(em);

        FormUpdateDto formUpdate = new FormUpdateDto("업데이트1", "업데이트1", null,
                null, 5, true, null,
                List.of(createContentCreate("text", null, "텍스트 블록-업데이트", null, false),
                        createContentCreate("short", "주관식 질문-업데이트", null, null, false),
                        createContentCreate("checkbox", "객관식 질문-업데이트", null, List.of("1", "2", "3"), false),
                        createContentCreate("text", null, "텍스트 블록-추가", null, false),
                        createContentCreate("long", "주관식 질문-추가", null, null, false),
                        createContentCreate("single", "객관식 질문-추가", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tagNew"),
                new RewardCreateDto("경품1-업데이트", rewardCategory.getRewardCategoryName(), 5),
                true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when
        tempFormUpdateService.execute(form.getId(), formUpdate, loginUserDto);
        //then
        EntityManagerUtil.flushAndClear(em);

        Form resultForm = em.find(Form.class, form.getId());
        AssertionUtil.assertWithSoftAssertions(s -> {
            s.assertThat(resultForm.getUser().getId()).isEqualTo(testUser.getId());
            s.assertThat(resultForm.getStartDate()).isNull();
            s.assertThat(resultForm.getStatus()).isEqualTo(FormStatus.NOT_STARTED);
            s.assertThat(resultForm.getDecorationList()).hasSize(2)
                    .extracting("detail", "position")
                    .containsExactly(Tuple.tuple("텍스트 블록-업데이트", 1),
                            Tuple.tuple("텍스트 블록-추가", 4));
            s.assertThat(resultForm.getDescriptiveQuestionList()).hasSize(2)
                    .extracting("title", "position")
                    .containsExactly(Tuple.tuple("주관식 질문-업데이트", 2),
                            Tuple.tuple("주관식 질문-추가", 5));
            s.assertThat(resultForm.getObjectiveQuestionList()).hasSize(2)
                    .extracting("title", "position")
                    .containsExactly(Tuple.tuple("객관식 질문-업데이트", 3),
                            Tuple.tuple("객관식 질문-추가", 6));

            List<Tag> tagList = resultForm.getFormTagList().stream()
                    .map(FormTag::getTag)
                    .toList();
            s.assertThat(tagList).hasSize(2)
                    .extracting("tagName", "count")
                    .containsExactlyInAnyOrder(
                            Tuple.tuple("tag1", 1),
                            Tuple.tuple("tagNew", 1));

            s.assertThat(resultForm.getReward().getRewardName()).isEqualTo("경품1-업데이트");
        });
    }
}