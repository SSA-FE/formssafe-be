package com.formssafe.global.error;

import com.formssafe.global.error.response.ErrorResponse;
import com.formssafe.global.error.response.ExceptionResponse;
import com.formssafe.global.error.type.BadRequestException;
import com.formssafe.global.error.type.BusinessException;
import com.formssafe.global.error.type.DataNotFoundException;
import com.formssafe.global.error.type.ForbiddenException;
import com.formssafe.global.error.type.SessionNotFoundException;
import com.formssafe.global.error.type.UserNotFoundException;
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
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode) {
        return new ResponseEntity<>(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage()),
                errorCode.getHttpStatus());
    }

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

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.FORBIDDEN.value(), e.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException e) {
        log.error("DataNotFoundException: ", e);
        return createErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("Error: ", e);
        return createErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error("Error: ", e);
        return new ResponseEntity<>(ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
