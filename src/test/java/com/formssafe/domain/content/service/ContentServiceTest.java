package com.formssafe.domain.content.service;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.formssafe.util.Fixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("[설문 문항 생성]")
class ContentServiceTest extends IntegrationTestConfig {
    private final EntityManager em;
    private final ContentService contentService;

    private User testUser;

    @Autowired
    public ContentServiceTest(EntityManager em,
                              ContentService contentService) {
        this.em = em;
        this.contentService = contentService;
    }

    @BeforeEach
    void setUp() {
        testUser = em.find(User.class, 1L);

        em.persist(createRewardCategory("커피"));
        EntityManagerUtil.flushAndClearContext(em);
    }

    @Nested
    class 설문_컨텐츠_생성 {
        @Test
        void 설문_컨텐츠를_등록할_수_있다() {
            Form form = createForm(testUser, "설문", "설명");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            List<ContentCreateDto> contentCreateDtos = Arrays.asList(
                    createContentCreate("single", "객관식 1개 선택", "객관식 질문1 입니다.", 1, List.of("1번문항", "2번문항"), true),
                    createContentCreate("checkbox", "객관식 체크 박스", "객관식 질문2 입니다.", 2, List.of("1번문항"), false),
                    createContentCreate("dropdown", "객관식 드롭다운", "객관식 질문3 입니다.", 3, List.of("1번문항"), true),
                    createContentCreate("short", "서술형 질문1", "주관식 질문1 입니다.", 4, null, true),
                    createContentCreate("long", "서술형 질문2", "주관식 질문2 입니다.", 5, null, false),
                    createContentCreate("text", "데코레이션", "데코레이션 1입니다.", 6, null, false)
            );

            //when
            contentService.createContents(contentCreateDtos, form);

            //then
            Form result = em.find(Form.class, form.getId());
            SoftAssertions.assertSoftly(softAssertions -> {
                List<Long> decorations = result.getDecorationList().stream()
                        .map(Decoration::getId)
                        .toList();

                List<Long> objectiveQuestions = result.getObjectiveQuestionList().stream()
                        .map(ObjectiveQuestion::getId)
                        .toList();

                List<Long> descriptiveQuestions = result.getDescriptiveQuestionList().stream()
                        .map(DescriptiveQuestion::getId)
                        .toList();

                softAssertions.assertThat(decorations).hasSize(1);
                softAssertions.assertThat(descriptiveQuestions).hasSize(2);
                softAssertions.assertThat(objectiveQuestions).hasSize(3);
            });
        }

        @Test
        void 설문_컨텐츠의_타입이_올바르지_않은_경우_예외가_발생한다() {
            Form form = createForm(testUser, "설문", "설명");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            List<ContentCreateDto> contents = List.of(createContentCreate("testType", "title", null, null, false));
            assertThatThrownBy(() -> contentService.createContents(contents, form))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_OPTION);
        }

        @Test
        void 객관식_질문의_보기가_존재하지_않을때_예외가_발생한다() {
            Form form = createForm(testUser, "설문", "설명");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            List<ContentCreateDto> contents = List.of(createContentCreate("single", "title", null, null, false));
            assertThatThrownBy(() -> contentService.createContents(contents, form))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.OBJECTIVE_QUESTION_REQUIRED_AT_LEAST_ONE_OPTION);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 101})
        void 설문_문항_제목이_1자_이상_100자_이하가_아니라면_예외가_발생한다(int titleLength) {
            //given
            Form form = createForm(testUser, "설문", "설명");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            String title = "a".repeat(titleLength);
            List<ContentCreateDto> contents = List.of(createContentCreate("short", title, null, null, false));
            //when then
            assertThatThrownBy(() -> contentService.createContents(contents, form))
                    .isInstanceOf(BadRequestException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_CONTENT_TITLE_LENGTH);
        }

        @Test
        void 설문_문항_설명이_2000자_이하가_아니라면_예외가_발생한다() {
            //given
            Form form = createForm(testUser, "설문", "설명");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            String description = "a".repeat(2001);
            List<ContentCreateDto> contents = List.of(createContentCreate("short", "title", description, null, false));
            //when then
            assertThatThrownBy(() -> contentService.createContents(contents, form))
                    .isInstanceOf(BadRequestException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_CONTENT_DESCRIPTION_LENGTH);
        }
    }


}