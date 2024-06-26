package com.formssafe.domain.activity.service;

import com.formssafe.domain.activity.dto.ActivityParam.SearchDto;
import com.formssafe.domain.activity.dto.ActivityResponse.FormListDto;
import com.formssafe.domain.activity.dto.ActivityResponse.FormListResponseDto;
import com.formssafe.domain.activity.dto.ActivityResponse.ParticipateSubmissionDto;
import com.formssafe.domain.form.dto.FormResponse.FormCursorDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.form.service.FormService;
import com.formssafe.domain.form.service.SortType;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionDetailResponseDto;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.submission.service.SubmissionService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.type.DataNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ActivityService {
    private final FormRepository formRepository;
    private final FormService formService;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;

    public FormListResponseDto getCreatedFormList(SearchDto param, LoginUserDto loginUser) {
        log.debug(param == null ? null : param.toString());
        User user = userRepository.getReferenceById(loginUser.id());

        List<Form> formByUserWithFiltered = formRepository.findFormByUserWithFiltered(param, user);

        return FormListResponseDto.from(formByUserWithFiltered.stream()
                .map(FormListDto::from)
                .toList(), FormCursorDto.from(SortType.from(param.sort()),
                formByUserWithFiltered.size() != 0 ? formByUserWithFiltered.get(
                        formByUserWithFiltered.size() - 1) : null));
    }

    public FormListResponseDto getParticipatedFormList(SearchDto param, LoginUserDto loginUser) {
        log.debug(param == null ? null : param.toString());
        User user = userRepository.getReferenceById(loginUser.id());

        List<Form> formByParticipateUserWithFiltered = formRepository.findFormByParticipateUserWithFiltered(param,
                user);

        return FormListResponseDto.from(formByParticipateUserWithFiltered.stream()
                .map(FormListDto::from)
                .toList(), FormCursorDto.from(SortType.from(param.sort()),
                formByParticipateUserWithFiltered.size() != 0 ? formByParticipateUserWithFiltered.get(
                        formByParticipateUserWithFiltered.size() - 1) : null));
    }

    public ParticipateSubmissionDto getSelfResponse(Long formId, LoginUserDto loginUser) {
        User user = userRepository.getReferenceById(loginUser.id());
        Form form = formService.getForm(formId);

        Submission submission = submissionRepository.findSubmissionByFormIDAndUserId(formId, loginUser.id())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NO_SUBMISSION_PARTICIPATED,
                        "해당 form에 대한 설문 응답이 존재하지 않습니다 : " + formId));

        List<SubmissionDetailResponseDto> submissionDetailDtos = getSubmissionDetail(submission);
        return ParticipateSubmissionDto.from(formId, submissionDetailDtos, submission.isTemp());
    }

    private List<SubmissionDetailResponseDto> getSubmissionDetail(Submission submission) {
        return submissionService.getSubmissionDetailDto(submission);
    }
}
