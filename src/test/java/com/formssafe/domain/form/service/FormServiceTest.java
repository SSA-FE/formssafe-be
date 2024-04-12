package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createDeletedForm;
import static com.formssafe.util.Fixture.createDeletedUser;
import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createFormWithEndDate;
import static com.formssafe.util.Fixture.createFormWithStatus;
import static com.formssafe.util.Fixture.createReward;
import static com.formssafe.util.Fixture.createRewardCategory;
import static com.formssafe.util.Fixture.createSubmissions;
import static com.formssafe.util.Fixture.createTemporaryForm;
import static com.formssafe.util.Fixture.createUser;
import static com.formssafe.util.Fixture.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.dto.FormResponse.FormDetailDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.reward.entity.RewardRecipient;
import com.formssafe.domain.reward.repository.RewardCategoryRepository;
import com.formssafe.domain.reward.repository.RewardRepository;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import com.formssafe.global.exception.type.ForbiddenException;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class FormServiceTest extends IntegrationTestConfig {
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final RewardRepository rewardRepository;
    private final RewardCategoryRepository rewardCategoryRepository;
    private final SubmissionRepository submissionRepository;
    private final FormService formService;
    private final EntityManager em;

    private User testUser;
    private RewardCategory rewardCategory;

    @Autowired
    public FormServiceTest(UserRepository userRepository,
                           FormRepository formRepository,
                           RewardRepository rewardRepository,
                           RewardCategoryRepository rewardCategoryRepository,
                           SubmissionRepository submissionRepository, FormService formService, EntityManager em) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.rewardRepository = rewardRepository;
        this.rewardCategoryRepository = rewardCategoryRepository;
        this.submissionRepository = submissionRepository;
        this.formService = formService;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(createUser("testUser"));
        rewardCategory = rewardCategoryRepository.save(createRewardCategory("경품 카테고리1"));
    }

    @Nested
    class 설문_상세_조회 {

        @Test
        void 설문을_상세_조회한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when
            FormDetailDto formDetail = formService.getFormDetail(form.getId(), loginUserDto);
            //then
            assertThat(formDetail).isNotNull()
                    .extracting("title", "description", "status", "questionCnt")
                    .contains("설문1", "설문설명1", "progress", 0);
        }

        @Test
        void 삭제된_설문_상세_조회시_예외가_발생한다() {
            //given
            Form form = formRepository.save(createDeletedForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.getFormDetail(form.getId(), loginUserDto))
                    .isInstanceOf(DataNotFoundException.class);
        }

        @Test
        void 작성자가아니고_임시등록된_설문_상세_조회시_예외가_발생한다() {
            //given
            Form form = formRepository.save(createTemporaryForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.getFormDetail(form.getId(), loginUserDto))
                    .isInstanceOf(DataNotFoundException.class);
        }
    }

    @Nested
    class 수동_설문_종료 {

        @Test
        void 수동으로_설문을_종료한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUser = new LoginUserDto(testUser.getId());
            //when
            formService.closeForm(form.getId(), loginUser);
            //then
            assertThat(form.getStatus()).isEqualTo(FormStatus.DONE);
        }

        @Test
        void 로그인유저와_설문작성자가_다르다면_예외가_발생한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUser = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.closeForm(form.getId(), loginUser))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        void 설문이_존재하지_않는다면_예외가_발생한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUser = new LoginUserDto(10L);
            //when then
            assertThatThrownBy(() -> formService.closeForm(form.getId() + 1, loginUser))
                    .isInstanceOf(DataNotFoundException.class);
        }

        @EnumSource(value = FormStatus.class, names = {"NOT_STARTED", "DONE", "REWARDED"})
        @ParameterizedTest
        void 설문이_진행중이_아니라면_예외가_발생한다(FormStatus status) {
            //given
            LocalDateTime endDate = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
            Form form = formRepository.save(
                    createFormWithEndDate(testUser, "설문1", "설문설명1", endDate, status));
            LoginUserDto loginUser = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.closeForm(form.getId(), loginUser))
                    .isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    class 설문_삭제 {

        @Test
        void 작성한_설문을_삭제한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when
            formService.deleteForm(form.getId(), loginUserDto);
            //then
            assertThat(form.isDeleted()).isTrue();
        }

        @Test
        void 이미_삭제된_설문_삭제시_예외가_발생한다() {
            //given
            Form form = formRepository.save(createDeletedForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.deleteForm(form.getId(), loginUserDto))
                    .isInstanceOf(DataNotFoundException.class);
        }

        @Test
        void 설문작성자와_로그인유저가_다르다면_예외가_발생한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.deleteForm(form.getId(), loginUserDto))
                    .isInstanceOf(ForbiddenException.class);
        }
    }

    @Nested
    class 설문_마감 {

        @Test
        void 경품없는_설문을_마감한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when
            formService.closeForm(form.getId(), loginUserDto);
            //then
            assertThat(form.getStatus()).isEqualTo(FormStatus.DONE);
            assertThat(form.getEndDate()).isNotNull();
        }

        @Test
        void 경품있는_설문을_마감한다() {
            //given
            List<User> users = createUsers(5);
            List<User> deletedUsers = List.of(
                    createDeletedUser("deleteUser1", "delete-oauthId1", "delete_email1@email.com"),
                    createDeletedUser("deleteUser2", "delete-oauthId1", "delete_email2@email.com"));
            userRepository.saveAll(users);
            userRepository.saveAll(deletedUsers);

            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            rewardRepository.save(createReward("경품1", form, rewardCategory, 5));

            List<Submission> submissions = createSubmissions(users, form);
            List<Submission> submissionsByDeletedUser = createSubmissions(deletedUsers, form);
            submissionRepository.saveAll(submissions);
            submissionRepository.saveAll(submissionsByDeletedUser);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());

            em.flush();
            em.clear();
            //when
            formService.closeForm(form.getId(), loginUserDto);
            //then
            em.flush();
            em.clear();

            form = formRepository.findById(form.getId()).orElseThrow(IllegalStateException::new);
            assertThat(form.getStatus()).isEqualTo(FormStatus.REWARDED);
            assertThat(form.getEndDate()).isNotNull();
            List<User> rewardRecipients = form.getRewardRecipientList().stream()
                    .map(RewardRecipient::getUser)
                    .toList();
            assertThat(rewardRecipients).hasSize(5)
                    .containsAll(users);
        }

        @Test
        void 설문작성자와_로그인유저가_다르다면_예외가_발생한다() {
            //given
            Form form = formRepository.save(createForm(testUser, "설문1", "설문설명1"));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.closeForm(form.getId(), loginUserDto))
                    .isInstanceOf(ForbiddenException.class);
        }

        @EnumSource(value = FormStatus.class, names = {"NOT_STARTED", "DONE", "REWARDED"})
        @ParameterizedTest
        void 진행중인_설문이_아니라면_예외가_발생한다(FormStatus status) {
            //given
            Form form = formRepository.save(createFormWithStatus(testUser, "설문1", "설문설명1", status));
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.closeForm(form.getId(), loginUserDto))
                    .isInstanceOf(BadRequestException.class);
        }
    }
}