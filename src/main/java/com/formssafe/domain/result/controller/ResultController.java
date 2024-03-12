package com.formssafe.domain.result.controller;

import com.formssafe.domain.user.dto.UserDto;
import com.formssafe.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/forms")
@RequiredArgsConstructor
@Tag(name = "result", description ="설문 응답 결과 조회 관련 api")
public class ResultController {

    @ApiResponse(responseCode = "200", description = "닉네임 변경 완료",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = "{\"userId\": 1, \"nickname\": \"exampleNickname\", \"imageUrl\": \"https://example.com/example.jpg\", \"email\": \"example@example.com\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("auth") String sessionId) {
        UserDto userProfile = new UserDto(1, "1", "https://1.com/1.jpg", "1@1.com");
        return ResponseEntity.ok(userProfile);
    }
}
