package com.formssafe.domain.submission.controller;

import com.formssafe.domain.auth.service.SessionService;
import com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionCreateDto;
import com.formssafe.domain.submission.service.SubmissionService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/forms")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "submission", description = "설문 참여 및 수정")
public class SubmissionController {
    private final SessionService sessionService;
    private final SubmissionService submissionService;

    @Operation(summary = "설문 참여하기", description = "처음으로 설문에 참여할 때 사용")
    @ApiResponse(responseCode = "200", description = "설문 응답 완료")
    @ApiResponse(responseCode = "400", description = "설문이 존재하지 않거나 만료되었을 때",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"설문이 존재하지 않거나 만료되었습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않는 경우",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @PostMapping("/{formId}/submission")
    @ResponseStatus(HttpStatus.OK)
    public void createSubmission(@PathVariable long formId, @RequestBody SubmissionCreateDto request,
                                 @AuthenticationPrincipal LoginUserDto loginUser) {
        submissionService.create(formId, request, loginUser);
    }

    @Operation(summary = "설문 참여하기(수정)", description = "임시 조정된 설문 참여")
    @ApiResponse(responseCode = "200", description = "설문 수정 완료")
    @ApiResponse(responseCode = "400", description = "설문이 존재하지 않거나 만료되었을 때",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"설문이 존재하지 않거나 만료되었습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않는 경우",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @PutMapping("/{formId}/submission")
    @ResponseStatus(HttpStatus.OK)
    public void modifySubmission(@PathVariable long formId, @RequestBody SubmissionCreateDto request,
                                 @AuthenticationPrincipal LoginUserDto loginUser) {
        submissionService.modify(formId, request, loginUser);
    }
}
