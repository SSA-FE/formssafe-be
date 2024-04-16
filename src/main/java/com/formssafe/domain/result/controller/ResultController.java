package com.formssafe.domain.result.controller;

import com.formssafe.domain.result.dto.ResultResponse;
import com.formssafe.domain.result.service.ExcelReportService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/forms")
@RequiredArgsConstructor
@Tag(name = "result", description = "설문 응답 결과 조회 관련 api")
public class ResultController {
    private final ExcelReportService excelReportService;

    @Operation(summary = "설문 결과 조회", description = "설문 응답 결과 전체 조회")
    @ApiResponse(responseCode = "200", description = "설문 응답결과 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResultResponse.class)))
    @ApiResponse(responseCode = "400", description = "formId가 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"formId가 존재하지 않습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/{formId}/result")
    public ResponseEntity<ResultResponse> getTotalResult(@RequestHeader("auth") String sessionId,
                                                         @PathVariable int formId) {
//        ResultResponse resultResponse = new ResultResponse(1, 1,new ArrayList<TotalResponse>(){{add(new TotalResponse(1, new ArrayList<Submission>(){{add(new Submission(1, 1));}}, LocalDateTime.now()));}});
//        return ResponseEntity.ok(resultResponse);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "설문 응답 결과 다운로드", description = "엑셀 파일 제공 예정")

    @ApiResponse(responseCode = "200", description = "설문 응답 결과 다운로드 링크 제공 or 자동 다운로드")
    @ApiResponse(responseCode = "400", description = "formId가 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"formId가 존재하지 않습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/{formId}/result/download")
    public void resultDownload(HttpServletResponse response, @PathVariable Long formId,
                               @AuthenticationPrincipal LoginUserDto loginUser) {
        excelReportService.execute(response, formId, loginUser);
    }
}
