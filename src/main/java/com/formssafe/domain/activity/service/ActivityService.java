package com.formssafe.domain.activity.service;

import com.formssafe.domain.activity.dto.ActivityParam.SearchDto;
import com.formssafe.domain.activity.dto.ActivityResponse.FormListDto;
import com.formssafe.domain.activity.dto.ActivityResponse.ParticipateSubmissionDto;
import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.repository.FormRepository;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionDetailResponseDto;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.submission.service.SubmissionService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.DataNotFoundException;
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
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;

    public List<FormListDto> getCreatedFormList(SearchDto param, LoginUserDto loginUser) {
        log.debug(param == null ? null : param.toString());

        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        List<Form> formByUserWithFiltered = formRepository.findFormByUserWithFiltered(param, user);

        return formByUserWithFiltered.stream()
                .map(FormListDto::from)
                .toList();
    }

    public List<FormListDto> getParticipatedFormList(SearchDto param, LoginUserDto loginUser) {
        log.debug(param == null ? null : param.toString());

        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        List<Form> formByParticipateUserWithFiltered = formRepository.findFormByParticipateUserWithFiltered(param,
                user);

        return formByParticipateUserWithFiltered.stream()
                .map(FormListDto::from)
                .toList();
    }

    public ParticipateSubmissionDto getSelfResponse(Long formId, LoginUserDto loginUser) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        Submission submission = submissionRepository.findSubmissionByFormIDAndUserId(formId, loginUser.id())
                .orElse(null);

        if (submission == null) {
            return null;
        }

        List<SubmissionDetailResponseDto> submissionDetailDtos = getSubmissionDetail(submission);
        return ParticipateSubmissionDto.from(formId, submissionDetailDtos, submission.isTemp());
    }

    private List<SubmissionDetailResponseDto> getSubmissionDetail(Submission submission) {
        return submissionService.getSubmissionDetailDto(submission);
    }
}
