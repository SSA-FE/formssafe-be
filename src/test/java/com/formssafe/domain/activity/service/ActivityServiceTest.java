package com.formssafe.domain.activity.service;

import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.activity.dto.ActivityParam.SearchDto;
import com.formssafe.domain.activity.dto.ActivityResponse.FormListResponseDto;
import com.formssafe.domain.activity.dto.ActivityResponse.ParticipateSubmissionDto;
import com.formssafe.domain.content.question.entity.*;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.submission.dto.SubmissionResponse;
import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.formssafe.util.Fixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("[등록한 설문/참여한 설문/참여한 응답 조회]")
class ActivityServiceTest extends IntegrationTestConfig {
    private final ActivityService activityService;
    private final EntityManager em;

    private User testUser;
    private User otherUser;

    @Autowired
    public ActivityServiceTest(ActivityService activityService, EntityManager em) {
        this.activityService = activityService;
        this.em = em;

    }

    @BeforeEach
    void setUp() {
        testUser = createUser("activityUser", "activityUser@example.com");
        em.persist(testUser);
        EntityManagerUtil.flushAndClearContext(em);

        otherUser = em.find(User.class, 2L);
    }

    @Nested
    class 내가_등록한_설문_전체_조회 {

        @Test
        void 로그인한_유저가_등록한_설문을_전체_조회한다() {
            //given
            List<Form> formList = new ArrayList<>();
            formList.add(createForm(testUser, "설문1", "설문설명1"));
            formList.add(createForm(testUser, "설문2", "설문설명2"));
            formList.add(createDeletedForm(testUser, "설문3", "설문설명3"));
            formList.add(createTemporaryForm(testUser, "설문4", "설문설명4"));
            formList.add(createTemporaryForm(otherUser, "설문5", "설문설명5"));
            formList.add(createForm(otherUser, "설문6", "설문설명6"));

            for (Form form : formList)
                em.persist(form);

            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUser = new LoginUserDto(testUser.getId());

            //when
            FormListResponseDto createdFormResponse = activityService.getCreatedFormList(SearchDto.createNull(),
                    loginUser);

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(createdFormResponse.forms()).hasSize(3)
                        .extracting("title")
                        .containsExactlyInAnyOrder("설문1", "설문2", "설문4");
            });
        }
    }

    @Nested
    class 내가_참여한_설문_전체_조회 {
        @Test
        void 로그인한_유저가_참여한_설문을_전체_조회한다() {
            //given
            User submissionUser = createUser("submissionUser", "submissionUser@example.com");
            em.persist(submissionUser);
            EntityManagerUtil.flushAndClearContext(em);

            List<Form> formList = new ArrayList<>();
            Form form1 = createForm(testUser, "설문1", "설문설명1");
            Form form2 = createForm(testUser, "설문2", "설문설명2");
            Form form3 = createDeletedForm(testUser, "설문3", "설문설명3");
            Form form4 = createTemporaryForm(testUser, "설문4", "설문설명4");
            Form form5 = createTemporaryForm(otherUser, "설문5", "설문설명5");
            Form form6 = createForm(otherUser, "설문6", "설문설명6");

            formList.add(form1);
            formList.add(form2);
            formList.add(form3);
            formList.add(form4);
            formList.add(form5);
            formList.add(form6);

            for (Form form : formList)
                em.persist(form);
            EntityManagerUtil.flushAndClearContext(em);

            List<Submission> submissionList = new ArrayList<>();
            submissionList.add(createSubmission(submissionUser, form1));
            submissionList.add(createTempSubmission(submissionUser, form2));
            submissionList.add(createSubmission(submissionUser, form6));

            for (Submission submission : submissionList)
                em.persist(submission);

            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUser = new LoginUserDto(submissionUser.getId());

            //when
            FormListResponseDto createdFormResponse = activityService.getParticipatedFormList(SearchDto.createNull(),
                    loginUser);

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(createdFormResponse.forms()).hasSize(3)
                        .extracting("title")
                        .containsExactlyInAnyOrder("설문1", "설문2", "설문6");
            });
        }
    }

    @Nested
    class 내가_참여한_응답_조회 {
        @Test
        void 로그인한_사용자가_참여한_설문의_응답을_조회한다() {
            //given
            Form testForm = createFormWithQuestionCnt(testUser, "설문 제목", "설문 설명", 2);
            em.persist(testForm);

            DescriptiveQuestion descriptiveQuestion = createDescriptiveQuestion(testForm, DescriptiveQuestionType.LONG, "주관식 설문 문항1", 1, true);
            ObjectiveQuestion objectiveQuestion = createObjectiveQuestion(testForm, ObjectiveQuestionType.SINGLE, "객관식 질문1", 4, List.of(new ObjectiveQuestionOption(1, "보기1"), new ObjectiveQuestionOption(2, "보기2")), true);

            em.persist(descriptiveQuestion);
            em.persist(objectiveQuestion);

            Submission submission = createSubmission(testUser, testForm);
            ObjectiveSubmission objectiveSubmission = createObjectiveSubmission(submission, "주관식 설문 문항 1", objectiveQuestion);
            DescriptiveSubmission descriptiveSubmission = createDescriptiveSubmission(submission, "3", descriptiveQuestion);

            em.persist(submission);
            em.persist(objectiveSubmission);
            em.persist(descriptiveSubmission);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());

            //when
            ParticipateSubmissionDto participateSubmissionDto = activityService.getSelfResponse(testForm.getId(), loginUserDto);

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(participateSubmissionDto).isNotNull();
                softAssertions.assertThat(participateSubmissionDto.formId()).isEqualTo(testForm.getId());
                softAssertions.assertThat(participateSubmissionDto.responses()).hasSize(2);

                SubmissionResponse.SubmissionDetailResponseDto response1 = participateSubmissionDto.responses().get(0);
                softAssertions.assertThat(response1.questionId()).isEqualTo(descriptiveQuestion.getUuid());

                SubmissionResponse.SubmissionDetailResponseDto response2 = participateSubmissionDto.responses().get(1);
                softAssertions.assertThat(response2.questionId()).isEqualTo(objectiveQuestion.getUuid());
            });
        }

        @Test
        void 로그인한_사용자가_선택한_설문에_대한_응답이_없는_경우_예외가_발생한다() {
            //given
            Form testForm = createFormWithQuestionCnt(testUser, "설문 제목", "설문 설명", 2);
            em.persist(testForm);
            EntityManagerUtil.flushAndClearContext(em);

            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());

            //when then
            assertThatThrownBy(() -> activityService.getSelfResponse(testForm.getId(), loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.NO_SUBMISSION_PARTICIPATED);
        }
    }
}