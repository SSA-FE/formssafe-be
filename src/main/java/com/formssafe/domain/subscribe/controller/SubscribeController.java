package com.formssafe.domain.subscribe.controller;

import com.formssafe.domain.activity.dto.SelfSubmissionResponse;
import com.formssafe.domain.subscribe.dto.SubscribeRequest.RewardListDto;
import com.formssafe.domain.subscribe.dto.SubscribeResponse.CategoryListDto;
import com.formssafe.domain.subscribe.service.SubscribeService;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscribe")
@AllArgsConstructor
public class SubscribeController {
    private final SubscribeService subscribeService;

    @Operation(summary = "경품 카테고리 목록 조회", description = "구독 상태 포함")
    @ApiResponse(responseCode = "200", description = "경품 카테고리 조회 성공(카테고리 없을 시 빈 배열)",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SelfSubmissionResponse.class)))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/reward")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryListDto> getRewardCategoryWithSubscribe(@AuthenticationPrincipal LoginUserDto loginUser) {
        return subscribeService.getRewardCategoryWithSubscribe(loginUser);
    }

    @Operation(summary = "경품 카테고리 구독 설정", description = "카테고리 ID 기반으로 설정, 요청 시 기존에 있던 데이터 삭제")
    @ApiResponse(responseCode = "200", description = "카테고리 구독 완료",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SelfSubmissionResponse.class)))
    @ApiResponse(responseCode = "400", description = "경품 카테고리가 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"입력한 경품 카테고리가 존재하지 않습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @PostMapping("/reward")
    @ResponseStatus(HttpStatus.OK)
    public void subscribeCategory(@AuthenticationPrincipal LoginUserDto loginUser,
                                  @RequestBody RewardListDto rewardListDto) {
        subscribeService.subscribeCategory(loginUser, rewardListDto);
    }
}
