package com.formssafe.domain.view.controller;


import com.formssafe.domain.form.dto.FormParam.SearchDto;
import com.formssafe.domain.form.dto.FormResponse.FormListDto;
import com.formssafe.domain.form.dto.FormResponse.FormListResponseDto;
import com.formssafe.domain.form.dto.FormResponse.FormWithQuestionDto;
import com.formssafe.domain.view.service.ViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "view", description = "설문 참여 조회 관련 API")
@RestController
@RequestMapping("/v1/view")
@Slf4j
@RequiredArgsConstructor
public class ViewController {
    private final ViewService viewService;

    @Operation(summary = "설문 전체 조회", description = "참여 가능한 설문을 목록으로 조회한다.")
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping(path = "/forms", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    FormListResponseDto getFormList(@ModelAttribute SearchDto param) {
        return viewService.getFormList(param);
    }

    @Operation(summary = "설문 상세 조회", description = "참여 가능한 id의 설문을 상세 조회한다.")
    @ApiResponse(responseCode = "400", description = "formId가 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"formId가 존재하지 않습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping(path = "/forms/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    FormWithQuestionDto getForm(@PathVariable Long id) {
        return viewService.getFormWithQuestion(id);
    }

    @Operation(summary = "Top10 핫한 설문 조회", description = "현재 시간을 기준으로 10분 이내에 저장된 top10 핫한 설문을 조회한다.")
    @ApiResponse(responseCode = "400", description = "formId가 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"formId가 존재하지 않습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping(path = "/forms/hot", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<FormListDto> getTop10HotForms() {
        return viewService.getTop10HotFormList();
    }
}
