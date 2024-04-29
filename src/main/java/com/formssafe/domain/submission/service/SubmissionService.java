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
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.global.error.type.UserNotFoundException;
import com.formssafe.global.util.DateTimeUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final DescriptiveQuestionService descriptiveQuestionService;
    private final ObjectiveQuestionService objectiveQuestionService;
    private final FormService formService;
    private final DescriptiveSubmissionRepository descriptiveSubmissionRepository;
    private final ObjectiveSubmissionRepository objectiveSubmissionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void create(long formId, SubmissionCreateDto request, LoginUserDto loginUser) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        if (user.isDeleted()) {
            throw new UserNotFoundException("해당 유저를 찾을 수 없습니다.:" + loginUser.id());
        }

        Form form = formService.getForm(formId);

        if (getSubmissionByUserAndForm(user, form) != null) {
            throw new BadRequestException(" 한 사용자가 하나의 설문에 대하여 두 개 이상의 응답을 작성할 수 없습니다.");
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
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        if (user.isDeleted()) {
            throw new UserNotFoundException("해당 유저를 찾을 수 없습니다.:" + loginUser.id());
        }

        Form form = formService.getForm(formId);

        Submission submission = getSubmissionByUserAndForm(user, form);
        if (submission == null) {
            throw new BadRequestException("등록되어 있는 응답이 존재하지 않습니다.");
        }
        if (!submission.isTemp()) {
            throw new BadRequestException("해당 응답은 완료된 응답입니다.");
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

    public SubmissionResponseDto getSubmission(long formId, LoginUserDto loginUser) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        if (user.isDeleted()) {
            throw new UserNotFoundException("해당 유저를 찾을 수 없습니다.:" + loginUser.id());
        }

        Submission submission = submissionRepository.findSubmissionByFormIDAndUserId(formId, loginUser.id())
                .orElse(null);

        if (submission == null) {
            return null;
        }

        List<SubmissionDetailResponseDto> submissionDetailResponseDtos = getSubmissionDetailDto(submission);
        return SubmissionResponseDto.from(formId, submissionDetailResponseDtos, submission.isTemp());
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
                    throw new BadRequestException("연관된 질문 타입이 올바르지 않습니다.");
                }
                objectiveSubmissions.add(dtos.toObjectiveSubmission(submission, objectiveQuestion));
                if (objectiveQuestion.isRequired()) {
                    requiredCnt++;
                }

            } else if (DescriptiveQuestionType.exists(dtos.type())) {
                DescriptiveQuestion descriptiveQuestion = descriptiveQuestionService.getDescriptiveQuestionByUuid(
                        dtos.questionId(), form.getId());
                if (!descriptiveQuestion.getQuestionType().displayName().equals(dtos.type())) {
                    throw new BadRequestException("연관된 질문 타입이 올바르지 않습니다.");
                }
                descriptiveSubmissions.add(dtos.toDescriptiveSubmission(submission, descriptiveQuestion));
                if (descriptiveQuestion.isRequired()) {
                    requiredCnt++;
                }
            } else {
                throw new BadRequestException("올바르지 않은 질문 type 입니다.");
            }
        }

        if (form.getQuestionCnt() < objectiveSubmissions.size() + descriptiveSubmissions.size()) {
            throw new BadRequestException("입력된 제출의 개수가 질문 문항보다 많습니다.");
        }

        if (requiredCnt != formService.getRequiredQuestionCnt(form)) {
            throw new BadRequestException("답변되지 않은 필수 문항이 존재합니다.");
        }
        descriptiveSubmissionRepository.saveAll(descriptiveSubmissions);
        objectiveSubmissionRepository.saveAll(objectiveSubmissions);
    }

    private void validate(User user, Form form) {
        if (form.getStatus() != FormStatus.PROGRESS) {
            throw new BadRequestException("참여하고자 하는 설문의 상태가 올바르지 않습니다.");
        }

        if (user.getId() == form.getUser().getId()) {
            throw new BadRequestException("자신이 작성한 설문에는 참여할 수 없습니다.");
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
