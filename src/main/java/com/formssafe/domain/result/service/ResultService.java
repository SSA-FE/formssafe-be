package com.formssafe.domain.result.service;

import com.formssafe.domain.form.entity.Form;
import com.formssafe.domain.form.service.FormService;
import com.formssafe.domain.result.dto.ResultResponse.ResultResponseDto;
import com.formssafe.domain.result.dto.ResultResponse.TotalResponse;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionDetailResponseDto;
import com.formssafe.domain.submission.entity.Submission;
import com.formssafe.domain.submission.repository.SubmissionRepository;
import com.formssafe.domain.submission.service.SubmissionService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.dto.UserResponse.UserListDto;
import com.formssafe.domain.user.entity.User;
import com.formssafe.domain.user.repository.UserRepository;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultService {
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;
    private final UserRepository userRepository;
    private final FormService formService;

    public ResultResponseDto getTotalResult(LoginUserDto loginUser, Long formId) {
        User user = userRepository.findById(loginUser.id())
                .orElseThrow(() -> new DataNotFoundException("해당 유저를 찾을 수 없습니다.: " + loginUser.id()));

        Form form = formService.getForm(formId);

        if (user.getId() != form.getUser().getId()) {
            throw new BadRequestException("자신이 작성한 설문만 설문 결과를 확인할 수 있습니다.");
        }

        List<TotalResponse> totalResponseList = new ArrayList<>();

        List<Submission> submissionList = submissionRepository.findAllByFormIdWithUser(formId);

        for (Submission submission : submissionList) {
            List<SubmissionDetailResponseDto> submissionDetailResponseDtoList = submissionService.getSubmissionDetailDto(
                    submission);
            totalResponseList.add(
                    new TotalResponse(submission.getId(), UserListDto.from(submission.getUser()),
                            submissionDetailResponseDtoList,
                            submission.getSubmitTime()));
        }
        return new ResultResponseDto(formId, form.getResponseCnt(), totalResponseList);
    }
}
