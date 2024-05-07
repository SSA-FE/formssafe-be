package com.formssafe.domain.form.service;

import static com.formssafe.util.Fixture.createDeletedForm;
import static com.formssafe.util.Fixture.createDeletedUser;
import static com.formssafe.util.Fixture.createForm;
import static com.formssafe.util.Fixture.createFormWithStatus;
import static com.formssafe.util.Fixture.createReward;
import static com.formssafe.util.Fixture.createRewardCategory;
import static com.formssafe.util.Fixture.createSubmissions;
import static com.formssafe.util.Fixture.createTemporaryForm;
import static com.formssafe.util.Fixture.createUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.form.dto.FormResponse.FormResultDto;
import com.formssafe.domain.form.dto.FormResponse.FormWithQuestionDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.reward.entity.Reward;
import com.formssafe.domain.reward.entity.RewardCategory;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("[설문 비즈니스 레이어 테스트]")
class FormServiceTest extends IntegrationTestConfig {
    private final FormService formService;
    private final EntityManager em;

    private User testUser;
    private RewardCategory rewardCategory;

    @Autowired
    public FormServiceTest(FormService formService,
                           EntityManager em) {
        this.formService = formService;
        this.em = em;
    }

    @BeforeEach
    void setUp() {
        testUser = em.find(User.class, 1L);

        rewardCategory = createRewardCategory("test_category");
        em.persist(rewardCategory);
        EntityManagerUtil.flushAndClearContext(em);
    }

    @Nested
    class 설문_결과_조회 {

        @Test
        void 작성자는_설문_결과를_조회할_수_있다() {
            //given
            Form form = createForm(testUser, "설문1", "설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when
            FormResultDto formResult = formService.getFormResult(form.getId(), loginUserDto);
            //then
            SoftAssertions.assertSoftly((s) -> {
                assertThat(formResult).isNotNull();
                assertThat(formResult.title()).isEqualTo("설문1");
                assertThat(formResult.description()).isEqualTo("설명1");
                assertThat(formResult.status()).isEqualTo("progress");
            });
        }

        @Test
        void 작성자가_아닌_사용자가_설문_결과_조회_시_예외가_발생한다() {
            //given
            Form form = createForm(testUser, "설문1", "설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.getFormResult(form.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_FORM_AUTHOR);
        }
    }

    @Nested
    class 설문_상세_조회 {

        @Test
        void 작성자는_임시_설문을_상세_조회할_수_있다() {
            //given
            Form form = createTemporaryForm(testUser, "설문1", "설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when then
            FormWithQuestionDto formWithQuestion = formService.getTempForm(form.getId(), loginUserDto);
            //then
            SoftAssertions.assertSoftly((s) -> {
                assertThat(formWithQuestion).isNotNull();
                assertThat(formWithQuestion.title()).isEqualTo("설문1");
                assertThat(formWithQuestion.description()).isEqualTo("설명1");
                assertThat(formWithQuestion.status()).isEqualTo("not_started");
            });
        }

        @Test
        void 삭제된_설문_상세_조회_시_예외가_발생한다() {
            //given
            Form form = createDeletedForm(testUser, "설문1", "설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.getFormResult(form.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.FORM_NOT_FOUND);
        }

        @Test
        void 작성자가_아닌_사용자가_임시_설문_상세_조회_시_예외가_발생한다() {
            //given
            Form form = createTemporaryForm(testUser, "설문1", "설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.getTempForm(form.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_FORM_AUTHOR);
        }

        @Test
        void 작성자가_임시_설문이_아닌_설문을_상세_조회할_시_예외가_발생한다() {
            //given
            Form form = createForm(testUser, "설문1", "설문설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.getTempForm(form.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.NOT_TEMP_FORM);
        }
    }

    @Nested
    class 설문_마감 {

        @Test
        void 설문_작성자는_경품_없는_설문을_마감할_수_있다() {
            //given
            Form form = createForm(testUser, "설문1", "설문설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when
            formService.closeForm(form.getId(), loginUserDto);
            //then
            Form result = em.find(Form.class, form.getId());
            SoftAssertions.assertSoftly((s) -> {
                assertThat(result.getStatus()).isEqualTo(FormStatus.DONE);
                assertThat(result.getEndDate()).isNotNull();
            });
        }

        @Test
        void 설문_작성자는_경품_있는_설문을_마감할_수_있다() {
            //given
            Form form = createForm(testUser, "설문1", "설문설명1");
            Reward reward = createReward("경품1", form, rewardCategory, 5);
            List<User> users = createUsers(5);
            List<User> deletedUsers = List.of(
                    createDeletedUser("deleteUser1", "delete-oauthId1", "delete_email1@email.com"),
                    createDeletedUser("deleteUser2", "delete-oauthId1", "delete_email2@email.com"));
            List<Submission> submissions = createSubmissions(users, form);
            List<Submission> submissionsByDeletedUser = createSubmissions(deletedUsers, form);

            em.persist(form);
            em.persist(reward);
            users.forEach(em::persist);
            deletedUsers.forEach(em::persist);
            submissions.forEach(em::persist);
            submissionsByDeletedUser.forEach(em::persist);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when
            formService.closeForm(form.getId(), loginUserDto);
            //then
            Form result = em.find(Form.class, form.getId());
            SoftAssertions.assertSoftly((s) -> {
                assertThat(result.getStatus()).isEqualTo(FormStatus.REWARDED);
                assertThat(result.getEndDate()).isNotNull();

                List<Long> rewardRecipientIds = result.getRewardRecipientList().stream()
                        .map(rewardRecipient -> rewardRecipient.getUser().getId())
                        .toList();

                List<Long> userIds = users.stream()
                        .map(User::getId)
                        .toList();

                assertThat(rewardRecipientIds).hasSize(5)
                        .containsAll(userIds);
            });


        }

        @Test
        void 설문이_존재하지_않는다면_예외가_발생한다() {
            //given
            Form form = createForm(testUser, "설문1", "설문설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUser = new LoginUserDto(10L);
            //when then
            assertThatThrownBy(() -> formService.closeForm(form.getId() + 1, loginUser))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.FORM_NOT_FOUND);
        }

        @Test
        void 로그인_유저가_설문_작성자가_아니라면_예외가_발생한다() {
            //given
            Form form = createForm(testUser, "설문1", "설문설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.closeForm(form.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_FORM_AUTHOR);
        }

        @EnumSource(value = FormStatus.class, names = {"NOT_STARTED", "DONE", "REWARDED"})
        @ParameterizedTest
        void 진행_중인_설문이_아니라면_예외가_발생한다(FormStatus status) {
            //given
            Form form = createFormWithStatus(testUser, "설문1", "설문설명1", status);
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.closeForm(form.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.NOT_PROGRESS_FORM);
        }
    }

    @Nested
    class 설문_삭제 {

        @Test
        void 작성한_설문을_삭제한다() {
            //given
            Form form = createForm(testUser, "설문1", "설문설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when
            formService.deleteForm(form.getId(), loginUserDto);
            //then
            Form result = em.find(Form.class, form.getId());
            assertThat(result.isDeleted()).isTrue();
        }

        @Test
        void 이미_삭제된_설문_삭제시_예외가_발생한다() {
            //given
            Form form = createDeletedForm(testUser, "설문1", "설문설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());
            //when then
            assertThatThrownBy(() -> formService.deleteForm(form.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.FORM_NOT_FOUND);
        }

        @Test
        void 설문_작성자와_로그인_유저가_다르다면_예외가_발생한다() {
            //given
            Form form = createForm(testUser, "설문1", "설문설명1");
            em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId() + 1);
            //when then
            assertThatThrownBy(() -> formService.deleteForm(form.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INVALID_FORM_AUTHOR);
        }
    }
}