package com.formssafe.domain.user.controller;

import com.formssafe.domain.user.dto.UserRequest.JoinDto;
import com.formssafe.domain.user.dto.UserRequest.LoginUserDto;
import com.formssafe.domain.user.dto.UserRequest.NicknameUpdateDto;
import com.formssafe.domain.user.dto.UserResponse.UserProfileDto;
import com.formssafe.domain.user.service.UserService;
import com.formssafe.global.exception.response.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "users", description = "사용자 관련 API")
public class UserController {
    private final UserService userService;

    @Operation(summary = "사이트 회원가입", description = "사이트 회원가입")
    @ApiResponse(responseCode = "200", description = "사이트 회원가입 완료")
    @ApiResponse(responseCode = "400", description = "닉네임 중복",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"중복된 닉네임이 존재합니다.\"}")))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public void join(@RequestBody JoinDto request,
                     @AuthenticationPrincipal LoginUserDto loginUser) {
        userService.join(request, loginUser);
    }

    @Operation(summary="프로필 가져오기", description="세션의 정보로부터 프로필 정보 가져와서 return")
    @ApiResponse(responseCode = "200", description = "프로필 불러오기 완료",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserProfileDto.class),
                    examples = @ExampleObject(value = "{\"userId\": 1, \"nickname\": \"exampleNickname\", \"imageUrl\": \"https://example.com/example.jpg\", \"email\": \"example@example.com\"}")
                    ))
    @ApiResponse(responseCode = "401", description = "세션이 존재하지 않음",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"세션이 존재하지 않습니다.\"}")))
    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto getUserProfile(@AuthenticationPrincipal LoginUserDto loginUser) {
        return userService.getProfile(loginUser);
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
    @PatchMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void updateNickname(HttpServletRequest request, @RequestBody NicknameUpdateDto nickname) {
        userService.updateNickname(request, nickname);
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
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(HttpServletRequest request, @PathVariable long userId) {
        userService.deleteAccount(request, userId);
    }
}
