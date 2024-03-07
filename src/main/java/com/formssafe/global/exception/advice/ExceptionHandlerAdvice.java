package com.formssafe.global.exception.advice;

import com.formssafe.global.exception.response.ExceptionResponse;
import com.formssafe.global.exception.type.SessionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {
        "com.formssafe.domain.auth.controller",
        "com.formssafe.domain.oauth.controller",
})
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleSessionNotFoundException(Exception e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }
}
