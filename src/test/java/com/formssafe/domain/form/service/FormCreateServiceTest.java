package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createContentCreate;
import static com.formssafe.util.Fixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.domain.batch.form.entity.FormBatchEnd;
import com.formssafe.domain.batch.form.entity.FormBatchStart;
import com.formssafe.domain.batch.form.repository.FormBatchEndRepository;
import com.formssafe.domain.batch.form.repository.FormBatchStartRepository;
import com.formssafe.domain.form.dto.FormRequest.FormCreateDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.dto.RewardRequest.RewardCreateDto;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.reward.repository.RewardRepository;
import com.formssafe.domain.tag.entity.Tag;
import com.formssafe.domain.tag.repository.TagRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.BadRequestException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class FormCreateServiceTest {
    @Autowired
    private FormCreateService formCreateService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private FormBatchEndRepository formBatchEndRepository;
    @Autowired
    private FormBatchStartRepository formBatchStartRepository;
    @Autowired
    private RewardCategoryRepository rewardCategoryRepository;

    @BeforeEach
    void setUp() {
        rewardCategoryRepository.save(RewardCategory.builder().rewardCategoryName("커피").build());
    }

    @Test
    void 바로시작하는_설문을_등록한다() {
        //given
        User user = userRepository.save(createUser("testUser"));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                startDate, endDate, 10,
                false, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        //when
        formCreateService.execute(formCreateDto, loginUserDto);

        //then
        List<Form> result = formRepository.findAll();
        assertThat(result).hasSize(1);

        Form resultForm = result.get(0);
        assertThat(resultForm.getUser().getId()).isEqualTo(user.getId());
        assertThat(resultForm.getStatus()).isEqualTo(FormStatus.PROGRESS);

        Tag tag13 = tagRepository.findByTagName("tag13").orElseThrow(IllegalStateException::new);
        assertThat(tag13.getCount()).isEqualTo(1);

        List<Reward> rewardResult = rewardRepository.findAll();
        assertThat(rewardResult).hasSize(1);

        Reward reward = rewardResult.get(0);
        assertThat(reward.getRewardName()).isEqualTo("경품1");

        List<FormBatchEnd> formBatchEndResult = formBatchEndRepository.findAll();
        assertThat(formBatchEndResult).hasSize(1);

        FormBatchEnd formBatchEnd = formBatchEndResult.get(0);
        assertThat(formBatchEnd.getServiceTime()).isEqualTo(endDate);
    }

    @Test
    void 몇분후_시작하는_설문을_등록한다() {
        //given
        User user = userRepository.save(createUser("testUser"));

        LocalDateTime startDate = LocalDateTime.now().plusMinutes(1);
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                startDate, endDate, 10,
                false, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        //when
        formCreateService.execute(formCreateDto, loginUserDto);

        //then
        List<Form> result = formRepository.findAll();
        assertThat(result).hasSize(1);

        Form resultForm = result.get(0);
        assertThat(resultForm.getUser().getId()).isEqualTo(user.getId());
        assertThat(resultForm.getStatus()).isEqualTo(FormStatus.NOT_STARTED);

        Tag tag13 = tagRepository.findByTagName("tag13").orElseThrow(IllegalStateException::new);
        assertThat(tag13.getCount()).isEqualTo(1);

        List<Reward> rewardResult = rewardRepository.findAll();
        assertThat(rewardResult).hasSize(1);

        Reward reward = rewardResult.get(0);
        assertThat(reward.getRewardName()).isEqualTo("경품1");

        List<FormBatchStart> formBatchStartResult = formBatchStartRepository.findAll();
        assertThat(formBatchStartResult).hasSize(1);

        FormBatchStart formBatchStart = formBatchStartResult.get(0);
        assertThat(formBatchStart.getServiceTime()).isEqualTo(startDate);
    }

    @Test
    void 경품카테고리가_유효하지않은_설문등록시_예외가_발생한다() {
        //given
        User user = userRepository.save(createUser("testUser"));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                startDate, endDate, 10,
                false, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "invalid", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문시각이_유효하지않은_설문등록시_예외가_발생한다() {
        //given
        User user = userRepository.save(createUser("testUser"));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                startDate, endDate, 10,
                false, null,
                List.of(createContentCreate("text", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "invalid", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문문항타입이_유효하지않은_설문등록시_예외가_발생한다() {
        //given
        User user = userRepository.save(createUser("testUser"));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                startDate, endDate, 10,
                false, null,
                List.of(createContentCreate("invalid", null, "텍스트 블록", null, false),
                        createContentCreate("short", "주관식 질문", null, null, false),
                        createContentCreate("checkbox", "객관식 질문", null, List.of("1", "2", "3"), false)),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 설문문항이없고_임시가아닌_설문등록시_예외가_발생한다() {
        //given
        User user = userRepository.save(createUser("testUser"));

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusDays(1L);
        FormCreateDto formCreateDto = new FormCreateDto("제목1", "설명1", null,
                startDate, endDate, 10,
                false, null,
                List.of(),
                List.of("tag1", "tag13"),
                new RewardCreateDto("경품1", "커피", 4),
                false);
        LoginUserDto loginUserDto = new LoginUserDto(user.getId());
        //when
        assertThatThrownBy(() -> formCreateService.execute(formCreateDto, loginUserDto))
                .isInstanceOf(BadRequestException.class);
    }
}