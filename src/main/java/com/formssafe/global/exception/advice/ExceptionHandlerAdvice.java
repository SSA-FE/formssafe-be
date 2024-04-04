package com.formssafe.global.exception.advice;

import com.formssafe.global.exception.response.ExceptionResponse;
import com.formssafe.global.exception.type.BadRequestException;
import com.formssafe.global.exception.type.DataNotFoundException;
import com.formssafe.global.exception.type.SessionNotFoundException;
import com.formssafe.global.exception.type.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {
        "com.formssafe.domain.auth.controller",
        "com.formssafe.domain.form.controller",
        "com.formssafe.domain.user.controller",
        "com.formssafe.domain.file.controller",
        "com.formssafe.domain.activity.controller",
        "com.formssafe.domain.result.controller",
        "com.formssafe.domain.submission.controller",
})
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleSessionNotFoundException(SessionNotFoundException e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDataNotFoundException(DataNotFoundException e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
