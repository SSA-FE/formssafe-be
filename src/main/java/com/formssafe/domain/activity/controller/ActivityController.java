package com.formssafe.domain.activity.controller;

import com.formssafe.domain.activity.dto.SelfSubmissionResponse;
import com.formssafe.domain.result.dto.ResultResponse;
import com.formssafe.domain.result.dto.TotalResponse;
import com.formssafe.domain.submission.dto.Response;
import com.formssafe.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/activity")
@RequiredArgsConstructor
@Tag(name = "activity", description ="자신의 활동 조회")
public class ActivityController {
    @Operation(summary="참여한 설문 조회", description="내가 참여한 설문 응답 조회")
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
    public ResponseEntity<SelfSubmissionResponse> getSelfResponse(@RequestHeader("auth") String sessionId, @PathVariable int formId) {
        SelfSubmissionResponse selfSubmissionResponse = new SelfSubmissionResponse(1, new ArrayList<Response>(){{add(new Response(1, 1));}}, true);
        //나중에 수정할 것임
        if(true) {
        return ResponseEntity.ok(selfSubmissionResponse);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }
}
