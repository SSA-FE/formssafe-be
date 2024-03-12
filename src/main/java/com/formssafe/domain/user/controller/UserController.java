package com.formssafe.domain.user.controller;

import com.formssafe.domain.user.dto.UserResponse;
import com.formssafe.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Tag(name = "users", description = "사용자 관련 API")
public class UserController {
    @Operation(summary="프로필 가져오기", description="세션의 정보로부터 프로필 정보 가져와서 return")
    @ApiResponse(responseCode = "200", description = "프로필 불러오기 완료",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.Profile.class),
                    examples = @ExampleObject(value = "{\"userId\": 1, \"nickname\": \"exampleNickname\", \"imageUrl\": \"https://example.com/example.jpg\", \"email\": \"example@example.com\"}")
                    ))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/profile")
    public ResponseEntity<UserResponse.Profile> getUserProfile(@RequestHeader("auth") String sessionId) {
        UserResponse.Profile userProfile = new UserResponse.Profile(1L, "1", "https://1.com/1.jpg", "1@1.com");
        return ResponseEntity.ok(userProfile);
    }

    @Operation(summary = "닉네임 변경하기", description = "변경하고자하는 닉네임을 Request로 받아서 변경시켜줌")
    @ApiResponse(responseCode = "200", description = "닉네임 변경 완료")
    @ApiResponse(responseCode = "400", description = "닉네임 중복",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"중복된 닉네임이 존재합니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @PatchMapping("/{userId}")
    public void patchNickname(@RequestHeader("auth") String sessionId, @PathVariable int userId) {
        // TODO: 3/11/24 닉네임 수정
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 완료")
    @ApiResponse(responseCode = "400", description = "userId가 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"userId가 존재하지 않습니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @ApiResponse(responseCode = "403", description = "사용자가 올바르지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"사용자가 올바르지 않습니다.\"}")))
    @DeleteMapping("/{userId}")
    public void deleteId(@RequestHeader("auth") String sessionId, @PathVariable int userId) {
        // TODO: 3/11/24 회원 탈퇴
    }
}
