package com.formssafe.domain.auth.controller;

import com.formssafe.domain.auth.service.SessionService;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.dto.AuthCode;
import com.formssafe.domain.oauth.service.OAuthService;
import com.formssafe.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "auth", description = "사용자 인증 및 로그아웃")
public class AuthController {

    private final SessionService sessionService;
    private final OAuthService oauthService;

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Oauth 요청", description = "서버 타입에 해당되는 Oauth 요청 페이지로 redirection, 테스트용")
    @GetMapping("/social/{oauthServerType}")
    void redirectAuthCodeRequestUrl(@PathVariable OauthServerType oauthServerType,
                                    HttpServletResponse response) throws IOException {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        log.debug("redirectUrl: {}", redirectUrl);

        response.sendRedirect(redirectUrl);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Oauth 회원가입/로그인", description = "사용자가 존재하지 않을 시 회원가입/존재할 시 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 완료(미회원가입시 회원가입 후 로그인)")
    @ApiResponse(responseCode = "400", description = "oauth code 오류")
    @PostMapping("/social/login/{oauthServerType}")
    void login(@PathVariable OauthServerType oauthServerType,
               @RequestBody AuthCode authCode,
               HttpServletRequest request) {
        User user = oauthService.loginOrSignup(oauthServerType, authCode.code());
        sessionService.createSession(request, user);
    }

    @Operation(summary = "로그아웃", description = "세션에 해당되는 사용자 로그아웃(세션 기록 삭제)")
    @ApiResponse(responseCode = "200", description = "로그아웃 완료")
    @ApiResponse(responseCode = "400", description = "session 미존재")
    @GetMapping("/logout")
    @ResponseBody
    void logout(HttpServletRequest request) {
        sessionService.deleteSession(request);
    }
}
