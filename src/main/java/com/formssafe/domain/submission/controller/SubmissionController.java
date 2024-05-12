package com.formssafe.domain.submission.controller;

import com.formssafe.domain.submission.dto.SubmissionRequest.SubmissionCreateDto;
import com.formssafe.domain.submission.dto.SubmissionResponse.SubmissionResponseDto;
import com.formssafe.domain.submission.service.SubmissionService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.global.error.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/forms")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "submission", description = "설문 참여 및 수정")
public class SubmissionController {
    private final SubmissionService submissionService;

    @Operation(summary = "설문 참여하기", description = "처음으로 설문에 참여할 때 사용")
    @ApiResponse(responseCode = "200", description = "설문 응답 완료")
    @ApiResponse(responseCode = "400", description = "설문이 존재하지 않거나 만료되었을 때",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"설문이 존재하지 않거나 만료되었습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않는 경우",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @PostMapping("/{formId}/submission")
    @ResponseStatus(HttpStatus.OK)
    public void createSubmission(@PathVariable long formId, @RequestBody SubmissionCreateDto request,
                                 @AuthenticationPrincipal LoginUserDto loginUser) {
        submissionService.create(formId, request, loginUser);
    }

    @Operation(summary = "설문 참여하기(수정)", description = "임시 저장된 설문 참여")
    @ApiResponse(responseCode = "200", description = "설문 수정 완료")
    @ApiResponse(responseCode = "400", description = "설문이 존재하지 않거나 만료되었을 때",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"설문이 존재하지 않거나 만료되었습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않는 경우",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @PutMapping("/{formId}/submission")
    @ResponseStatus(HttpStatus.OK)
    public void modifySubmission(@PathVariable long formId, @RequestBody SubmissionCreateDto request,
                                 @AuthenticationPrincipal LoginUserDto loginUser) {
        submissionService.modify(formId, request, loginUser);
    }

    @Operation(summary = "작성한 임시 설문 응답 가져오기", description = "설문 수정을 위해 기존 설문 응답을 조회")
    @ApiResponse(responseCode = "200", description = "설문 수정 완료")
    @ApiResponse(responseCode = "400", description = "설문이 존재하지 않거나 만료되었을 때",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"설문이 존재하지 않거나 만료되었습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않는 경우",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/{formId}/submission")
    public ResponseEntity<SubmissionResponseDto> getTempSumbission(@PathVariable long formId,
                                                                   @AuthenticationPrincipal LoginUserDto loginUser) {
        SubmissionResponseDto submissionResponseDto = submissionService.getTempSubmission(formId, loginUser);
        if (submissionResponseDto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(submissionResponseDto);
    }
}
