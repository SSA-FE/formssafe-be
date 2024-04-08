package com.formssafe.domain.submission.service;

import com.formssafe.domain.content.question.entity.DescriptiveQuestion;
import com.formssafe.domain.content.question.entity.DescriptiveQuestionType;
import com.formssafe.domain.content.question.entity.ObjectiveQuestion;
import com.formssafe.domain.content.question.entity.ObjectiveQuestionType;
import com.formssafe.domain.content.question.service.DescriptiveQuestionService;
import com.formssafe.domain.content.question.service.ObjectiveQuestionService;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.service.FormService;
import com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionCreateDto;
import com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionDetailDto;
import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.repository.DescriptiveSubmissionRepository;
import com.formssafe.domain.submission.repository.ObjectiveSubmissionRepository;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public void create(long formId, SubmissionCreateDto request, LoginUserDto loginUser) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        Form form = formService.getForm(formId);

        if (getSubmissionByUserAndForm(user, form) != null) {
            throw new BadRequestException("한 사용자가 하나의 설문에 대하여 두 개 이상의 응답을 작성할 수 없습니다.");
        }

        Submission submission = createSubmission(request, user, form);

        createDetailSubmission(request.submissions(), submission, formId);

        if (!request.isTemp()) {
            form.increaseResponseCount();
        }
    }

    @Transactional
    public void modify(long formId, SubmissionCreateDto request, LoginUserDto loginUser) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));
        
        Form form = formService.getForm(formId);

        Submission preSubmission = getSubmissionByUserAndForm(user, form);
        if (preSubmission == null) {
            throw new BadRequestException("등록되어 있는 응답이 존재하지 않습니다.");
        }
        if (!preSubmission.isTemp()) {
            throw new BadRequestException("해당 응답은 완료된 응답입니다.");
        }

        submissionRepository.deleteById(preSubmission.getId());

        Submission submission = createSubmission(request, user, form);

        createDetailSubmission(request.submissions(), submission, formId);

        if (!request.isTemp()) {
            form.increaseResponseCount();
        }
    }

    private Submission getSubmissionByUserAndForm(User user, Form form) {
        return submissionRepository.findSubmissionByFormIDAndUserId(form.getId(), user.getId()).orElse(null);
    }

    private Submission createSubmission(SubmissionCreateDto request, User user, Form form) {
        Submission submission = request.toSubmission(user, form);
        submission = submissionRepository.save(submission);
        return submission;
    }

    private void createDetailSubmission(List<SubmissionDetailDto> submissionDetailDtos, Submission submission,
                                        Long formId) {
        List<ObjectiveSubmission> objectiveSubmissions = new ArrayList<>();
        List<DescriptiveSubmission> descriptiveSubmissions = new ArrayList<>();

        for (SubmissionDetailDto dtos : submissionDetailDtos) {
            if (ObjectiveQuestionType.exists(dtos.type())) {
                ObjectiveQuestion objectiveQuestion = objectiveQuestionService.getObjectiveQuestionByUuid(
                        dtos.questionId(), formId);
                if (!objectiveQuestion.getQuestionType().displayName().equals(dtos.type())) {
                    throw new BadRequestException("연관된 질문 타입이 올바르지 않습니다.");
                }
                objectiveSubmissions.add(dtos.toObjectiveSubmission(submission, objectiveQuestion));
            } else if (DescriptiveQuestionType.exists(dtos.type())) {
                DescriptiveQuestion descriptiveQuestion = descriptiveQuestionService.getDescriptiveQuestionByUuid(
                        dtos.questionId(), formId);
                if (!descriptiveQuestion.getQuestionType().displayName().equals(dtos.type())) {
                    throw new BadRequestException("연관된 질문 타입이 올바르지 않습니다.");
                }
                descriptiveSubmissions.add(dtos.toDescriptiveSubmission(submission, descriptiveQuestion));
            } else {
                throw new BadRequestException("올바르지 않은 질문 type 입니다.");
            }
        }
        descriptiveSubmissionRepository.saveAll(descriptiveSubmissions);
        objectiveSubmissionRepository.saveAll(objectiveSubmissions);
    }
}
