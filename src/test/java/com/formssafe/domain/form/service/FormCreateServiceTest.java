package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createContentCreate;
import static com.formssafe.util.Fixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.tag.entity.FormTag;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.tag.repository.TagRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.error.type.BadRequestException;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormCreateServiceTest extends IntegrationTestConfig {
    private final FormCreateService formCreateService;
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final RewardCategoryRepository rewardCategoryRepository;
    private final TagRepository tagRepository;
    private final EntityManager em;

    private User testUser;

    @Autowired
    public FormCreateServiceTest(FormCreateService formCreateService, UserRepository userRepository,
                                 FormRepository formRepository, RewardCategoryRepository rewardCategoryRepository,
                                 TagRepository tagRepository, EntityManager em) {
        this.formCreateService = formCreateService;
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.rewardCategoryRepository = rewardCategoryRepository;
        this.tagRepository = tagRepository;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        rewardCategoryRepository.save(RewardCategory.builder().rewardCategoryName("커피").build());
        testUser = userRepository.save(createUser("testUser"));
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
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        em.flush();
        em.clear();
        //when
        formCreateService.execute(formCreateDto, loginUserDto);
        //then
        em.flush();
        em.clear();

        List<Form> result = formRepository.findAll();
        assertThat(result).hasSize(1);

        Form resultForm = result.get(0);
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
                new RewardCreateDto("경품1", "커피", 4),
                true);
        LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
        em.flush();
        em.clear();
        //when
        formCreateService.execute(formCreateDto, loginUserDto);
        //then
        em.flush();
        em.clear();

        List<Form> result = formRepository.findAll();
        assertThat(result).hasSize(1);

        Form resultForm = result.get(0);
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
        LocalDateTime endDate = startDate.minusDays(1L);
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
        LocalDateTime endDate = startDate.minusDays(1L);
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
}