package com.formssafe.domain.submission.service;

import com.formssafe.domain.content.decoration.entity.DecorationType;
import com.formssafe.domain.content.question.entity.*;
import com.formssafe.domain.content.question.service.DescriptiveQuestionService;
import com.formssafe.domain.content.question.service.ObjectiveQuestionService;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.service.FormService;
import com.formssafe.domain.submission.dto.SubmissionRequest.*;
import com.formssafe.domain.submission.entity.DescriptiveSubmission;
import com.formssafe.domain.submission.entity.ObjectiveSubmission;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.repository.DescriptiveSubmissionRepository;
import com.formssafe.domain.submission.repository.ObjectiveSubmissionRepository;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public void create(long formId, SubmissionCreateDto request, LoginUserDto loginUser){
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다.: " + loginUser.id()));

        Form form = formService.getForm(formId);
        log.info(form.toString());
        Submission submission = createSubmission(request, user, form);
        log.info(submission.toString());

        createDetailSubmission(request.submissions(), submission);
    }

    private Submission createSubmission(SubmissionCreateDto request, User user, Form form){
        Submission submission = request.toSubmission(user, form);
        submission = submissionRepository.save(submission);
        return submission;
    }
    private void createDetailSubmission(List<SubmissionDetailDto> submissionDetailDtos, Submission submission){
        List<ObjectiveSubmission> objectiveSubmissions = new ArrayList<>();
        List<DescriptiveSubmission> descriptiveSubmissions = new ArrayList<>();

        for(SubmissionDetailDto dtos: submissionDetailDtos){
            if(ObjectiveQuestionType.exists(dtos.type())){
                ObjectiveQuestion objectiveQuestion = objectiveQuestionService.getObjectiveQuestionByUuid(dtos.questionId());
                objectiveSubmissions.add(dtos.toObjectiveSubmission(submission, objectiveQuestion));
            }
            else if(DescriptiveQuestionType.exists(dtos.type())){
                DescriptiveQuestion descriptiveQuestion = descriptiveQuestionService.getDescriptiveQuestionByUuid(dtos.questionId());
                descriptiveSubmissions.add(dtos.toDescriptiveSubmission(submission, descriptiveQuestion));
            }
//            else{
//
//            }
        }
        descriptiveSubmissionRepository.saveAll(descriptiveSubmissions);
        objectiveSubmissionRepository.saveAll(objectiveSubmissions);
    }
}
