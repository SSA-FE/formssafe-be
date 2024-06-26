package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createContentCreate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.content.dto.ContentRequest.ContentCreateDto;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.dto.FormResponse.FormIdDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.tag.repository.TagRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
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

class FormCreateServiceTest extends IntegrationTestConfig {
    private final FormCreateService formCreateService;
    private final TagRepository tagRepository;
    private final EntityManager em;

    private User testUser;
    private RewardCategory rewardCategory;

    @Autowired
    public FormCreateServiceTest(FormCreateService formCreateService,
                                 TagRepository tagRepository,
                                 EntityManager em) {
        this.formCreateService = formCreateService;
        this.tagRepository = tagRepository;
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
    void 설문을_등록한다() {
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
        assertThat(resultForm.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(resultForm.getStatus()).isEqualTo(FormStatus.PROGRESS);
        assertThat(resultForm.getDecorationList().get(0).getPosition()).isEqualTo(1);
        assertThat(resultForm.getDescriptiveQuestionList().get(0).getPosition()).isEqualTo(2);
        assertThat(resultForm.getObjectiveQuestionList().get(0).getPosition()).isEqualTo(3);

        Tag tag13 = tagRepository.findByTagName("tag13").orElseThrow(IllegalStateException::new);
        assertThat(tag13.getCount()).isEqualTo(1);

        assertThat(resultForm.getReward().getRewardName()).isEqualTo("경품1");
    }

    @Test
    void 임시설문을_등록한다() {
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
        assertThat(resultForm.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(resultForm.getStatus()).isEqualTo(FormStatus.NOT_STARTED);
        assertThat(resultForm.getStartDate()).isNull();
        assertThat(resultForm.getDecorationList().get(0).getPosition()).isEqualTo(1);
        assertThat(resultForm.getDescriptiveQuestionList().get(0).getPosition()).isEqualTo(2);
        assertThat(resultForm.getObjectiveQuestionList().get(0).getPosition()).isEqualTo(3);

        List<Tag> tagList = resultForm.getFormTagList().stream()
                .map(FormTag::getTag)
                .toList();
        for (Tag tag : tagList) {
            assertThat(tag.getCount()).isEqualTo(1);
        }

        assertThat(resultForm.getReward().getRewardName()).isEqualTo("경품1");
    }

    @Test
    void 경품카테고리가_유효하지않은_설문등록시_예외가_발생한다() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문시각이_유효하지않은_설문등록시_예외가_발생한다() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 개인정보폐기시각이_유효하지않은_설문등록시_예외가_발생한다() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문문항타입이_유효하지않은_설문등록시_예외가_발생한다() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문문항이없고_임시가아닌_설문등록시_예외가_발생한다() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("임시 설문 제목이 비었거나 100자 초과인 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void fail_InvalidTempFormTitle(int titleLength) {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_TITLE_LENGTH);
    }

    @DisplayName("설문 제목이 비었거나 100자 초과인 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void fail_InvalidFormTitle(int titleLength) {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_TITLE_LENGTH);
    }

    @DisplayName("임시 설문 설명이 2000자 초과인 경우 예외가 발생한다")
    @Test
    void fail_InvalidTempFormDescription() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_DESCRIPTION_LENGTH);
    }

    @DisplayName("설문 설명이 2000자 초과인 경우 예외가 발생한다")
    @Test
    void fail_InvalidFormDescription() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_DESCRIPTION_LENGTH);
    }

    @DisplayName("임시 설문 이미지가 5개를 초과한 경우 예외가 발생한다")
    @Test
    void fail_InvalidTempFormTotalImageSize() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TOTAL_IMAGE_SIZE);
    }

    @DisplayName("설문 이미지가 5개를 초과한 경우 예외가 발생한다")
    @Test
    void fail_InvalidFormTotalImageSize() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TOTAL_IMAGE_SIZE);
    }

    @DisplayName("임시 설문 예상 시간이 1440분 초과인 경우 예외가 발생한다")
    @Test
    void fail_InvalidTempFormExpectTime() {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TEMP_FORM_EXPECT_TIME);
    }

    @DisplayName("설문 예상 시간이 1분 이상 1440분 이하가 아니라면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 1441})
    void fail_InvalidFormExpectTime(int expectTime) {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_FORM_EXPECT_TIME);
    }

    @DisplayName("설문 질문 문항이 1개 이상 100개 이하가 아니라면 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void fail_InvalidQuestionCount(int questionCnt) {
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
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_QUESTION_SIZE);
    }
}