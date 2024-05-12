package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createContentCreate;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormResponse.FormIdDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.util.AssertionUtil;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("[설문 등록/임시 등록 비즈니스 레이어 테스트]")
class FormCreateServiceTest extends IntegrationTestConfig {
    private final FormCreateService formCreateService;
    private final EntityManager em;

    private User testUser;
    private RewardCategory rewardCategory;

    @Autowired
    public FormCreateServiceTest(FormCreateService formCreateService,
                                 EntityManager em) {
        this.formCreateService = formCreateService;
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
    void 사용자는_설문을_등록할_수_있다() {
        //given
        LocalDateTime startDate = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", rewardCategory.getRewardCategoryName(), 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when
        FormIdDto result = formCreateService.execute(formCreateDto, loginUserDto);
        EntityManagerUtil.flushAndClear(em);
        //then
        Form resultForm = em.find(Form.class, result.formId());
        AssertionUtil.assertWithSoftAssertions(s -> {
            s.assertThat(resultForm.getUser().getId()).isEqualTo(testUser.getId());
            s.assertThat(resultForm.getStatus()).isEqualTo(FormStatus.PROGRESS);
            s.assertThat(resultForm.getDecorationList().get(0).getPosition()).isEqualTo(1);
            s.assertThat(resultForm.getDescriptiveQuestionList().get(0).getPosition()).isEqualTo(2);
            s.assertThat(resultForm.getObjectiveQuestionList().get(0).getPosition()).isEqualTo(3);

            em.createQuery("select t from Tag t where t.tagName in :tagNames", Tag.class)
                    .setParameter("tagNames", List.of("tag1", "tag13"))
                    .getResultList()
                    .forEach(tag -> s.assertThat(tag.getCount()).isEqualTo(1));
            s.assertThat(resultForm.getReward().getRewardName()).isEqualTo("경품1");
        });
    }

    @Test
    void 사용자는_임시_설문을_등록할_수_있다() {
        //given
        LocalDateTime startDate = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", rewardCategory.getRewardCategoryName(), 4),
                true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when
        FormIdDto result = formCreateService.execute(formCreateDto, loginUserDto);
        EntityManagerUtil.flushAndClear(em);
        //then
        Form resultForm = em.find(Form.class, result.formId());
        AssertionUtil.assertWithSoftAssertions(s -> {
            s.assertThat(resultForm.getUser().getId()).isEqualTo(testUser.getId());
            s.assertThat(resultForm.getStatus()).isEqualTo(FormStatus.NOT_STARTED);
            s.assertThat(resultForm.getDecorationList().get(0).getPosition()).isEqualTo(1);
            s.assertThat(resultForm.getDescriptiveQuestionList().get(0).getPosition()).isEqualTo(2);
            s.assertThat(resultForm.getObjectiveQuestionList().get(0).getPosition()).isEqualTo(3);

            em.createQuery("select t from Tag t where t.tagName in :tagNames", Tag.class)
                    .setParameter("tagNames", List.of("tag1", "tag13"))
                    .getResultList()
                    .forEach(tag -> s.assertThat(tag.getCount()).isEqualTo(1));
            s.assertThat(resultForm.getReward().getRewardName()).isEqualTo("경품1");
        });
    }

    @Test
    void 경품_카테고리가_유효하지_않은_설문_등록_시_예외가_발생한다() {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "invalid", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문_마감_시각이_유효하지_않은_설문_등록_시_예외가_발생한다() {
        //given
        LocalDateTime endDate = LocalDateTime.now();
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"), null,
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 개인_정보_폐기_시각이_유효하지_않은_설문_등록_시_예외가_발생한다() {
        //given
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime privacyDisposalDate = LocalDateTime.now().minusMinutes(1L);

        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                endDate, 10, privacyDisposalDate,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"), null,
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문_문항_타입이_유효하지_않은_설문_등록_시_예외가_발생한다() {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                endDate, 10, null,
                List.of(createContentCreate("invalid", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"), null, false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문_문항이_없고_임시가_아닌_설문_등록시_예외가_발생한다() {
        //given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                endDate, 10, null,
                List.of(),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void 임시_설문_제목이_비었거나_100자_초과인_경우_예외가_발생한다(int titleLength) {
        String title = "a".repeat(titleLength);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto(title, "설명1", null,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_TITLE_LENGTH);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void 설문_제목이_비었거나_100자_초과인_경우_예외가_발생한다(int titleLength) {
        String title = "a".repeat(titleLength);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto(title, "설명1", null,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_TITLE_LENGTH);
    }

    @Test
    void 임시_설문_설명이_2000자_초과인_경우_예외가_발생한다() {
        String description = "a".repeat(2001);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("설문1", description, null,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_DESCRIPTION_LENGTH);
    }

    @Test
    void 설문_설명이_2000자_초과인_경우_예외가_발생한다() {
        String description = "a".repeat(2001);
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("설문1", description, null,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_DESCRIPTION_LENGTH);
    }

    @Test
    void 임시_설문_이미지가_5개를_초과한_경우_예외가_발생한다() {
        List<String> images = List.of("image1", "image2", "image3", "image4", "image5", "image6");
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("설문1", "설명1", images,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TOTAL_IMAGE_SIZE);
    }

    @Test
    void 설문_이미지가_5개를_초과한_경우_예외가_발생한다() {
        List<String> images = List.of("image1", "image2", "image3", "image4", "image5", "image6");
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("설문1", "설명1", images,
                endDate, 10, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TOTAL_IMAGE_SIZE);
    }

    @Test
    void 임시_설문_예상_시간이_1440분_초과인_경우_예외가_발생한다() {
        int expectTime = 1441;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("설문1", "설명1", null,
                endDate, expectTime, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TEMP_FORM_EXPECT_TIME);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1441})
    void 설문_예상_시간이_1분_이상_1440분_이하가_아니라면_예외가_발생한다(int expectTime) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("설문1", "설명1", null,
                endDate, expectTime, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_EXPECT_TIME);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void 설문_질문_문항이_1개_이상_100개_이하가_아니라면_예외가_발생한다(int questionCnt) {
        //given
        List<ContentCreateDto> questions = new ArrayList<>();
        for (int i = 0; i < questionCnt; ++i) {
            questions.add(createContentCreate("short", "질문" + i, null, null, false));
        }

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("설문1", "설명1", null,
                endDate, 5, null, questions,
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        //when then
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_QUESTION_SIZE);
    }
}