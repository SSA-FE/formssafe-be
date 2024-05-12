package com.formssafe.domain.submission.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.formssafe.config.IntegrationTestConfig;
import com.formssafe.domain.content.decoration.entity.Decoration;
import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.question.entity.*;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionCreateDto;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionDetailResponseDto;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionResponseDto;
import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.util.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionDetailDto;
import static com.formssafe.util.Fixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("[설문 참여/임시 참여 응답 조회/임시 참여 응답 수정]")
class SubmissionServiceTest extends IntegrationTestConfig {
    private static final Logger log = LoggerFactory.getLogger(SubmissionServiceTest.class);
    private final EntityManager em;
    private final SubmissionService submissionService;

    private User testUser;
    private User submissionUser;
    private Form testForm;
    private DescriptiveQuestion descriptiveQuestion1;
    private DescriptiveQuestion descriptiveQuestion2;
    private Decoration decoration;
    private ObjectiveQuestion objectiveQuestion1;
    private ObjectiveQuestion objectiveQuestion2;
    private ObjectiveQuestion objectiveQuestion3;


    @Autowired
    public SubmissionServiceTest(final EntityManager em,
                                 final SubmissionService submissionService) {
        this.em = em;
        this.submissionService = submissionService;
    }

    @BeforeEach
    void before() {
        testUser = em.find(User.class, 1L);
        submissionUser = createUser("submissionUser", "submissionUser@example.com");
        em.persist(submissionUser);

        testForm = createFormWithQuestionCnt(testUser, "설문 제목", "설문 설명", 5);
        em.persist(testForm);

        descriptiveQuestion1 = createDescriptiveQuestion(testForm, DescriptiveQuestionType.LONG, "주관식 설문 문항1", 1, true);
        descriptiveQuestion2 = createDescriptiveQuestion(testForm, DescriptiveQuestionType.SHORT, "주관식 설문 문항2", 2, false);
        decoration = createDecoration(testForm, DecorationType.TEXT, "설문 데코레이션1", 3);
        objectiveQuestion1 = createObjectiveQuestion(testForm, ObjectiveQuestionType.SINGLE, "객관식 질문1", 4, List.of(new ObjectiveQuestionOption(1, "보기1"), new ObjectiveQuestionOption(2, "보기2")), true);
        objectiveQuestion2 = createObjectiveQuestion(testForm, ObjectiveQuestionType.DROPDOWN, "객관식 질문2", 5, List.of(new ObjectiveQuestionOption(1, "보기1")), false);
        objectiveQuestion3 = createObjectiveQuestion(testForm, ObjectiveQuestionType.CHECKBOX, "객관식 질문3", 6, List.of(new ObjectiveQuestionOption(1, "보기1"), new ObjectiveQuestionOption(2, "보기2"), new ObjectiveQuestionOption(3, "보기3")), false);

        em.persist(descriptiveQuestion1);
        em.persist(descriptiveQuestion2);
        em.persist(decoration);
        em.persist(objectiveQuestion1);
        em.persist(objectiveQuestion2);
        em.persist(objectiveQuestion3);
        EntityManagerUtil.flushAndClearContext(em);
    }

    @Nested
    class 설문_참여 {
        @Test
        void 사용자는_설문에_대한_응답을_작성할_수_있다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto descriptiveSubmission2 = new SubmissionDetailDto("short", descriptiveQuestion2.getUuid(), "주관식 설문 문항2 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);
            SubmissionDetailDto objectiveSubmission2 = new SubmissionDetailDto("dropdown", objectiveQuestion2.getUuid(), 1);
            SubmissionDetailDto objectiveSubmission3 = new SubmissionDetailDto("checkbox", objectiveQuestion3.getUuid(), new int[]{1, 2});

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(descriptiveSubmission2);
            submissionDetailDtos.add(objectiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission2);
            submissionDetailDtos.add(objectiveSubmission3);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, false);

            //when
            submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto);

            //then
            Submission submission = submissionService.getSubmission(testForm.getId(), loginUserDto);
            ObjectMapper objectMapper = new ObjectMapper();
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(submission).isNotNull();
                softAssertions.assertThat(submission.getForm().getId()).isEqualTo(testForm.getId());
                softAssertions.assertThat(submission.getUser().getId()).isEqualTo(submissionUser.getId());
                softAssertions.assertThat(submission.getDescriptiveSubmissionList()).hasSize(2);
                softAssertions.assertThat(submission.getObjectiveSubmissionList()).hasSize(3);

                Map<String, String> descriptiveAnswers = submission.getDescriptiveSubmissionList().stream()
                        .collect(Collectors.toMap(
                                descriptiveSubmission -> descriptiveSubmission.getDescriptiveQuestion().getUuid(), DescriptiveSubmission::getContent
                        ));
                softAssertions.assertThat(descriptiveAnswers.get(descriptiveQuestion1.getUuid())).isEqualTo("주관식 설문 문항1 답변");
                softAssertions.assertThat(descriptiveAnswers.get(descriptiveQuestion2.getUuid())).isEqualTo("주관식 설문 문항2 답변");

                submission.getObjectiveSubmissionList().forEach(objectiveSubmission -> {
                    if (objectiveSubmission.getObjectiveQuestion().getQuestionType().displayName().equals("single") || objectiveSubmission.getObjectiveQuestion().getQuestionType().displayName().equals("dropdown")) {
                        // 객관식 단일 선택 검증
                        Integer selectedOption = Integer.parseInt(objectiveSubmission.getContent());
                        softAssertions.assertThat(selectedOption).isNotNull();
                    } else if (objectiveSubmission.getObjectiveQuestion().getQuestionType().displayName().equals("checkbox")) {
                        // 체크박스 선택 검증
                        try {
                            List<Integer> selectedOptions = objectMapper.readValue(objectiveSubmission.getContent(), new TypeReference<List<Integer>>() {
                            });
                            softAssertions.assertThat(selectedOptions).hasSize(2);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            });
        }

        @Test
        void 사용자는_설문에_대한_임시_응답을_작성할_수_있다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto);

            //then
            Submission submission = submissionService.getSubmission(testForm.getId(), loginUserDto);
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(submission).isNotNull();
                softAssertions.assertThat(submission.isTemp()).isTrue();
                softAssertions.assertThat(submission.getForm().getId()).isEqualTo(testForm.getId());
                softAssertions.assertThat(submission.getUser().getId()).isEqualTo(submissionUser.getId());
            });
        }

        @Test
        void 사용자가_하나의_설문에_대해_두_개_이상의_응답을_작성할_시_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            Submission createSubmission = createSubmission(submissionUser, testForm);
            em.persist(createSubmission);
            EntityManagerUtil.flushAndClearContext(em);

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ONLY_ONE_SUBMISSION_ALLOWED);
        }

        @ParameterizedTest
        @EnumSource(value = FormStatus.class, names = {"NOT_STARTED", "DONE", "REWARDED"})
        void 응답하고자_하는_설문의_상태가_진행중이_아닐시_예외가_발생한다(FormStatus status) {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            Form notProgressTestForm = createFormWithStatus(testUser, "설문 제목", "설문 설명", status);
            em.persist(notProgressTestForm);
            EntityManagerUtil.flushAndClearContext(em);

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(notProgressTestForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.FORM_STATUS_NOT_IN_PROGRESS);
        }

        @Test
        void 사용자가_응답하고자_하는_설문이_자신이_만든_설문일시_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(testUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CANNOT_SUBMIT_FORM_YOU_CREATED);
        }

        @Test
        void 사용자가_응답한_객관식_문항이_존재하지_않는_경우_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", "randomUUID", 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.OBJECTIVE_QUESTION_NOT_EXIST);
        }

        @ParameterizedTest
        @ValueSource(strings = {"short", ""})
        void 사용자가_응답한_객관식_문항의_설문_타입과_응답의_설문_타입이_같지_않는_경우_예외가_발생한다(String questionType) {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto(questionType, descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.SUBMISSION_TYPE_MISMATCH);
        }

        @Test
        void 사용자가_응답한_주관식_문항이_존재하지_않는_경우_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", "randomUUID", "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.DESCRIPTIVE_QUESTION_NOT_EXIST);
        }

        @ParameterizedTest
        @ValueSource(strings = {"checkbox", "dropdown", ""})
        void 사용자가_응답한_주관식_문항의_설문_타입과_응답의_설문_타입이_같지_않는_경우_예외가_발생한다(String questionType) {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto(questionType, objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.SUBMISSION_TYPE_MISMATCH);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 501})
        void 사용자가_응답한_short형_주관식_문항의_응답이_1자_이상_500자_이하가_아니라면_예외가_발생한다(int submissionLength) {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            String content = "a".repeat(submissionLength);
            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("short", descriptiveQuestion2.getUuid(), content);
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.SHORT_QUESTION_SUBMISSION_CONTENT_OVER_LIMIT);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 5001})
        void 사용자가_응답한_long형_주관식_문항의_응답이_1자_이상_5000자_이하가_아니라면_예외가_발생한다(int submissionLength) {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            String content = "a".repeat(submissionLength);
            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), content);
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.LONG_QUESTION_SUBMISSION_CONTENT_OVER_LIMIT);
        }

        @Test
        void 사용자가_작성한_응답이_문항의_갯수보다_많을경우_예외가_발생한다() {
            //ErrorCode.ENTRY_SUBMITTED_EXCEEDS_QUESTIONS
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto descriptiveSubmission2 = new SubmissionDetailDto("short", descriptiveQuestion2.getUuid(), "주관식 설문 문항2 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);
            SubmissionDetailDto objectiveSubmission2 = new SubmissionDetailDto("dropdown", objectiveQuestion2.getUuid(), 1);
            SubmissionDetailDto objectiveSubmission3 = new SubmissionDetailDto("checkbox", objectiveQuestion3.getUuid(), new int[]{1, 2});

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(descriptiveSubmission2);
            submissionDetailDtos.add(objectiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission2);
            submissionDetailDtos.add(objectiveSubmission3);
            submissionDetailDtos.add(objectiveSubmission3);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ENTRY_SUBMITTED_EXCEEDS_QUESTIONS);
        }

        @Test
        void 사용자가_작성한_필수_응답의_갯수가_필수_문항의_갯수보다_적을_경우_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 문항1 답변");

            submissionDetailDtos.add(descriptiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);

            //when
            assertThatThrownBy(() -> submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.REQUIRED_QUESTIONS_UNANSWERED);
        }
    }

    @Nested
    class 임시_참여_응답_조회 {
        @Test
        void 사용자가_자신의_임시_응답_설문에_대한_조회를_할_수_있다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);
            submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto);

            //when
            SubmissionResponseDto submissionResponseDto = submissionService.getTempSubmission(testForm.getId(), loginUserDto);

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(submissionResponseDto).isNotNull();
                softAssertions.assertThat(submissionResponseDto.isTemp()).isTrue();
                softAssertions.assertThat(submissionResponseDto.formId()).isEqualTo(testForm.getId());
                softAssertions.assertThat(submissionResponseDto.responses()).hasSize(2);

                SubmissionDetailResponseDto response1 = submissionResponseDto.responses().get(0);
                softAssertions.assertThat(response1.questionId()).isEqualTo(descriptiveQuestion1.getUuid());

                SubmissionDetailResponseDto response2 = submissionResponseDto.responses().get(1);
                softAssertions.assertThat(response2.questionId()).isEqualTo(objectiveQuestion1.getUuid());
            });
        }

        @Test
        void 사용자가_저장한_임시_응답이_존재하지_않을_경우_null을_반환한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            //when
            SubmissionResponseDto submissionResponseDto = submissionService.getTempSubmission(testForm.getId(), loginUserDto);

            //then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(submissionResponseDto).isNull();
            });
        }
    }

    @Nested
    class 임시_참여_응답_수정 {
        @Test
        void 사용자는_임시_응답_설문에_대한_응답을_작성할_수_있다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, true);
            submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto);

            //when
            submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, false);
            submissionService.modify(testForm.getId(), submissionCreateDto, loginUserDto);

            //then
            Submission submission = submissionService.getSubmission(testForm.getId(), loginUserDto);
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(submission).isNotNull();
                softAssertions.assertThat(submission.isTemp()).isEqualTo(false);
            });
        }

        @Test
        void 사용자가_해당_설문에_작성한_임시_응답이_존재하지_않을_시_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, false);

            //when then
            assertThatThrownBy(() -> submissionService.modify(testForm.getId(), submissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.NO_EXISTING_SUBMISSION_FOUND);
        }

        @Test
        void 사용자가_해당_설문에_작성한_응답이_최종_응답일_경우_예외가_발생한다() {
            //given
            LoginUserDto loginUserDto = new LoginUserDto(submissionUser.getId());

            List<SubmissionDetailDto> submissionDetailDtos = new ArrayList<>();
            SubmissionDetailDto descriptiveSubmission1 = new SubmissionDetailDto("long", descriptiveQuestion1.getUuid(), "주관식 설문 문항1 답변");
            SubmissionDetailDto objectiveSubmission1 = new SubmissionDetailDto("single", objectiveQuestion1.getUuid(), 2);

            submissionDetailDtos.add(descriptiveSubmission1);
            submissionDetailDtos.add(objectiveSubmission1);

            SubmissionCreateDto submissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, false);
            submissionService.create(testForm.getId(), submissionCreateDto, loginUserDto);

            SubmissionCreateDto modifySubmissionCreateDto = new SubmissionCreateDto(submissionDetailDtos, false);

            //when then
            assertThatThrownBy(() -> submissionService.modify(testForm.getId(), modifySubmissionCreateDto, loginUserDto))
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.NOT_TEMPORARY_SUBMISSION);

        }
    }
}