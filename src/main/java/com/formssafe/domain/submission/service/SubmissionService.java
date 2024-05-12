package com.formssafe.domain.submission.service;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionType;
import com.formssafe.domain.content.question.service.DescriptiveQuestionService;
import com.formssafe.domain.content.question.service.ObjectiveQuestionService;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.entity.FormStatus;
import com.formssafe.domain.form.service.FormService;
import com.formssafe.domain.notification.dto.NotificationEventDto.FormParticipantsNotificationEventDto;
import com.formssafe.domain.notification.event.type.FormParticipantsNotificationEvent;
import com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionCreateDto;
import com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionDetailDto;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionDetailResponseDto;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionResponseDto;
import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.repository.DescriptiveSubmissionRepository;
import com.formssafe.domain.submission.repository.ObjectiveSubmissionRepository;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.service.UserService;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.global.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserService userService;
    private final DescriptiveQuestionService descriptiveQuestionService;
    private final ObjectiveQuestionService objectiveQuestionService;
    private final FormService formService;
    private final SubmissionValidateService submissionValidateService;
    private final DescriptiveSubmissionRepository descriptiveSubmissionRepository;
    private final ObjectiveSubmissionRepository objectiveSubmissionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void create(long formId, SubmissionCreateDto request, LoginUserDto loginUser) {
        User user = userService.getUserById(loginUser.id());

        Form form = formService.getForm(formId);

        if (getSubmissionByUserAndForm(user, form) != null) {
            throw new BadRequestException(ErrorCode.ONLY_ONE_SUBMISSION_ALLOWED,
                    "한 사용자가 하나의 설문에 대하여 두 개 이상의 응답을 작성할 수 없습니다.");
        }

        validate(user, form);

        Submission submission = createSubmission(request, user, form);

        createDetailSubmission(request.submissions(), submission, form);

        if (!request.isTemp()) {
            form.increaseResponseCount();
            applicationEventPublisher.publishEvent(
                    new FormParticipantsNotificationEvent(new FormParticipantsNotificationEventDto(form, user), this));
        }
    }

    @Transactional
    public void modify(long formId, SubmissionCreateDto request, LoginUserDto loginUser) {
        User user = userService.getUserById(loginUser.id());

        Form form = formService.getForm(formId);

        Submission submission = getSubmissionByUserAndForm(user, form);

        if (submission == null) {
            throw new BadRequestException(ErrorCode.NO_EXISTING_SUBMISSION_FOUND);
        }
        if (!submission.isTemp()) {
            throw new BadRequestException(ErrorCode.NOT_TEMPORARY_SUBMISSION);
        }

        validate(user, form);

        deleteDetailSubmission(submission);

        submission.update(request.isTemp(), LocalDateTime.now());

        createDetailSubmission(request.submissions(), submission, form);

        if (!request.isTemp()) {
            form.increaseResponseCount();
            applicationEventPublisher.publishEvent(
                    new FormParticipantsNotificationEvent(new FormParticipantsNotificationEventDto(form, user), this));
        }
    }

    public SubmissionResponseDto getTempSubmission(long formId, LoginUserDto loginUser) {
        User user = userService.getUserById(loginUser.id());

        Submission submission = submissionRepository.findSubmissionByFormIDAndUserId(formId, loginUser.id())
                .orElse(null);

        if (submission == null) {
            return null;
        }

        List<SubmissionDetailResponseDto> submissionDetailResponseDtos = getSubmissionDetailDto(submission);
        return SubmissionResponseDto.from(formId, submissionDetailResponseDtos, submission.isTemp());
    }

    public Submission getSubmission(long formId, LoginUserDto loginUser) {
        return submissionRepository.findSubmissionByFormIDAndUserId(formId, loginUser.id()).orElse(null);
    }

    public List<SubmissionDetailResponseDto> getSubmissionDetailDto(Submission submission) {
        List<Object> submissions = new ArrayList<>();
        submissions.addAll(getDescriptiveSubmissionFromSubmission(submission));
        submissions.addAll(getObjectiveSubmissionFromSubmission(submission));

        return submissions.stream()
                .map(SubmissionDetailResponseDto::from)
                .toList();
    }

    private void deleteDetailSubmission(Submission submission) {
        descriptiveSubmissionRepository.deleteAllBySubmissionId(submission.getId());
        objectiveSubmissionRepository.deleteAllBySubmissionId(submission.getId());
    }

    private Submission getSubmissionByUserAndForm(User user, Form form) {
        return submissionRepository.findSubmissionByFormIDAndUserId(form.getId(), user.getId()).orElse(null);
    }

    private List<DescriptiveSubmission> getDescriptiveSubmissionFromSubmission(Submission submission) {
        return descriptiveSubmissionRepository.findAllByResponseId(
                submission.getId());
    }

    private List<ObjectiveSubmission> getObjectiveSubmissionFromSubmission(Submission submission) {
        return objectiveSubmissionRepository.findAllByResponseId(submission.getId());
    }

    private Submission createSubmission(SubmissionCreateDto request, User user, Form form) {
        Submission submission = request.toSubmission(user, form);
        submission = submissionRepository.save(submission);
        return submission;
    }

    private void createDetailSubmission(List<SubmissionDetailDto> submissionDetailDtos, Submission submission,
                                        Form form) {
        List<ObjectiveSubmission> objectiveSubmissions = new ArrayList<>();
        List<DescriptiveSubmission> descriptiveSubmissions = new ArrayList<>();
        int requiredCnt = 0;

        for (SubmissionDetailDto dtos : submissionDetailDtos) {
            if (ObjectiveQuestionType.exists(dtos.type())) {
                ObjectiveQuestion objectiveQuestion = objectiveQuestionService.getObjectiveQuestionByUuid(
                        dtos.questionId(), form.getId());
                if (!objectiveQuestion.getQuestionType().displayName().equals(dtos.type())) {
                    throw new BadRequestException(ErrorCode.SUBMISSION_TYPE_MISMATCH,
                            "연관된 질문 타입이 올바르지 않습니다. : " + dtos.type());
                }
                objectiveSubmissions.add(dtos.toObjectiveSubmission(submission, objectiveQuestion));
                if (objectiveQuestion.isRequired()) {
                    requiredCnt++;
                }

            } else if (DescriptiveQuestionType.exists(dtos.type())) {
                DescriptiveQuestion descriptiveQuestion = descriptiveQuestionService.getDescriptiveQuestionByUuid(
                        dtos.questionId(), form.getId());
                if (!descriptiveQuestion.getQuestionType().displayName().equals(dtos.type())) {
                    throw new BadRequestException(ErrorCode.SUBMISSION_TYPE_MISMATCH, "연관된 질문 타입이 올바르지 않습니다.");
                }
                DescriptiveSubmission descriptiveSubmission = dtos.toDescriptiveSubmission(submission,
                        descriptiveQuestion);
                submissionValidateService.validDescriptiveSubmission(descriptiveSubmission,
                        descriptiveQuestion.getQuestionType());
                descriptiveSubmissions.add(descriptiveSubmission);
                if (descriptiveQuestion.isRequired()) {
                    requiredCnt++;
                }
            } else {
                throw new BadRequestException(ErrorCode.SUBMISSION_TYPE_MISMATCH);
            }
        }

        if (form.getQuestionCnt() < objectiveSubmissions.size() + descriptiveSubmissions.size()) {
            throw new BadRequestException(ErrorCode.ENTRY_SUBMITTED_EXCEEDS_QUESTIONS);
        }

        if (requiredCnt != formService.getRequiredQuestionCnt(form)) {
            throw new BadRequestException(ErrorCode.REQUIRED_QUESTIONS_UNANSWERED);
        }
        descriptiveSubmissionRepository.saveAll(descriptiveSubmissions);
        objectiveSubmissionRepository.saveAll(objectiveSubmissions);

        //TODO : 확인 필요
        submission.getDescriptiveSubmissionList().addAll(descriptiveSubmissions);
        submission.getObjectiveSubmissionList().addAll(objectiveSubmissions);
    }

    private void validate(User user, Form form) {
        if (form.getStatus() != FormStatus.PROGRESS) {
            throw new BadRequestException(ErrorCode.FORM_STATUS_NOT_IN_PROGRESS);
        }

        if (user.getId() == form.getUser().getId()) {
            throw new BadRequestException(ErrorCode.CANNOT_SUBMIT_FORM_YOU_CREATED);
        }
    }

    @Transactional
    public void disposalPrivacy(LocalDateTime now) {
        log.info("disposal privacy start");
        now = DateTimeUtil.truncateSecondsAndNanos(now);

        List<Long> objectiveQuestions = objectiveQuestionService.getObjectiveQuestionByDisposalTime(now);
        List<Long> descriptiveQuestions = descriptiveQuestionService.getDescriptiveQuestionIdByDisposalTime(now);

        log.info("dispose objective submission : {}", objectiveQuestions);
        log.info("dispose descriptive submission : {}", descriptiveQuestions);

        for (Long objectiveQuestionId : objectiveQuestions) {
            objectiveSubmissionRepository.deleteAllPrivacyByQuestionId(objectiveQuestionId);
        }
        for (Long descriptiveQuestionId : descriptiveQuestions) {
            descriptiveSubmissionRepository.deleteAllPrivacyByQuestionId(descriptiveQuestionId);
        }

        log.info("disposal privacy end");
    }
}
