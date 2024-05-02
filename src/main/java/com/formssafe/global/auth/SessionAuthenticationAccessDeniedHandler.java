package com.formssafe.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formssafe.global.error.ErrorCode;
import com.formssafe.global.error.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@RequiredArgsConstructor
@Slf4j
public class SessionAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setStatus(ErrorCode.INVALID_USER.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_USER.getCode(),
                ErrorCode.INVALID_USER.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
