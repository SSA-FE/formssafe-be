package com.formssafe.domain.oauth.controller;

import com.formssafe.domain.auth.service.SessionService;
import com.formssafe.domain.member.entity.Member;
import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.dto.AuthCode;
import com.formssafe.domain.oauth.service.OAuthService;
import com.formssafe.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
@Slf4j
public class OauthController {

    private final OAuthService oauthService;
    private final SessionService sessionService;

    @SneakyThrows
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(@PathVariable OauthServerType oauthServerType,
                                                    HttpServletResponse response) {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        log.debug("redirectUrl: {}", redirectUrl);

        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login/{oauthServerType}")
    void login(@PathVariable OauthServerType oauthServerType,
               @RequestBody AuthCode authCode,
               HttpServletResponse response) {
        Member member = oauthService.loginOrSignup(oauthServerType, authCode.code());
        String session = sessionService.createSession(member);
        CookieUtil.addCookie(response, "auth", session, -1);
    }
}
