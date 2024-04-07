package com.formssafe.domain.activity.controller;

import com.formssafe.domain.activity.dto.ActivityParam;
import com.formssafe.domain.activity.dto.ActivityResponse.FormListDto;
import com.formssafe.domain.activity.dto.SelfSubmissionResponse;
import com.formssafe.domain.activity.service.ActivityService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/activity")
@RequiredArgsConstructor
@Tag(name = "activity", description = "내 활동 조회 API")
public class ActivityController {

    private final ActivityService activityService;

    @Operation(summary = "참여한 설문 조회", description = "내가 참여한 설문 응답 조회")
    @ApiResponse(responseCode = "200", description = "나의 응답 조회 성공(미응답시 빈 Response)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SelfSubmissionResponse.class)))
    @ApiResponse(responseCode = "400", description = "formId가 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"formId가 존재하지 않습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/forms/{formId}/responses")
    public ResponseEntity<SelfSubmissionResponse> getSelfResponse(@PathVariable int formId) {
        return null;
    }

    @Operation(summary = "내가 등록한 설문 전체 조회", description = "내가 등록한 설문을 목록으로 조회한다.")
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping(path = "/forms", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<FormListDto> getCreatedFormList(@ModelAttribute ActivityParam.SearchDto param,
                                                @AuthenticationPrincipal LoginUserDto loginUser) {
        return activityService.getCreatedFormList(param, loginUser);
    }

    @Operation(summary = "내가 참여한 설문 전체 조회", description = "내가 참여한 설문을 목록으로 조회한다.")
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping(path = "/responses", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<FormListDto> getParticipatedFormList(@ModelAttribute ActivityParam.SearchDto param,
                                                     @AuthenticationPrincipal LoginUserDto loginUser) {
        return activityService.getParticipatedFormList(param, loginUser);
    }
}
