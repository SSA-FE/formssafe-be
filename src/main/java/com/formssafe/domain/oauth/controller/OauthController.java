package com.formssafe.domain.oauth.controller;

import com.formssafe.domain.oauth.OauthServerType;
import com.formssafe.domain.oauth.dto.AuthCode;
import com.formssafe.domain.oauth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OauthController {

    private final OAuthService oauthService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(@PathVariable OauthServerType oauthServerType,
                                                    HttpServletResponse response) {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        log.info("redirectUrl: {}", redirectUrl);

        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/{oauthServerType}")
    ResponseEntity<Void> login(@PathVariable OauthServerType oauthServerType,
                               @RequestBody AuthCode authCode) {
        log.info("oauthServerType: {}, authCode: {}", oauthServerType, authCode);
        oauthService.loginOrSignup(oauthServerType, authCode.code());
        return ResponseEntity.ok().build();
    }
}
