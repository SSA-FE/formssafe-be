//package com.formssafe.domain.oauth.controller;
//
//import com.formssafe.domain.auth.service.SessionService;
//import com.formssafe.domain.user.entity.User;
//import com.formssafe.domain.oauth.OauthServerType;
//import com.formssafe.domain.oauth.dto.AuthCode;
//import com.formssafe.domain.oauth.service.OAuthService;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@RequiredArgsConstructor
//@Slf4j
//@Tag(name="oauth", description = "Oauth 관련 API")
//public class OauthController {
//
//    private final OAuthService oauthService;
//    private final SessionService sessionService;
//
//    @ResponseStatus(HttpStatus.FOUND)
//    @GetMapping("/{oauthServerType}")
//    void redirectAuthCodeRequestUrl(@PathVariable OauthServerType oauthServerType,
//                                    HttpServletResponse response) throws IOException {
//        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
//        log.debug("redirectUrl: {}", redirectUrl);
//
//        response.sendRedirect(redirectUrl);
//    }
//
//    @ResponseStatus(HttpStatus.OK)
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "로그인 완료(미회원가입시 회원가입 후 로그인)"),
//            @ApiResponse(responseCode = "400", description = "oauth code 오류")
//    })
//    @PostMapping("/login/{oauthServerType}")
//    void login(@PathVariable OauthServerType oauthServerType,
//               @RequestBody AuthCode authCode,
//               HttpServletRequest request) {
//        User user = oauthService.loginOrSignup(oauthServerType, authCode.code());
//        sessionService.createSession(request, user);
//    }
//}
