package com.formssafe.domain.auth.controller;

import com.formssafe.domain.auth.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final SessionService sessionService;

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    void logout(HttpServletRequest request) {
        sessionService.deleteSession(request);
    }
}
