package com.formssafe.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    /**
     * 디폴트 에러
     */
    SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "SYSTEM000", "서비스에 장애가 발생했습니다."),
    /**
     * Session 에러
     */
    SESSION_NOT_FOUND(NOT_FOUND, "SESSION000", "사용자 세션이 존재하지 않습니다."),
    /**
     * 인증
     */
    UNAUTHORIZED_ACCESS(UNAUTHORIZED, "AUTH000", "권한이 없습니다"),

    /**
     * 사용자
     */
    USER_NOT_FOUND(BAD_REQUEST, "USER000", "해당 유저가 존재하지 않습니다."),
    INVALID_USER(FORBIDDEN, "USER001", "권한이 없습니다."),

    /**
     * 설문
     */
    FORM_NOT_FOUND(BAD_REQUEST, "FORM000", "해당 설문이 존재하지 않습니다."),
    INVALID_AUTHOR(FORBIDDEN, "FORM001", "작성자가 아닙니다."),

    /**
     * 경품 카테고리
     */
    REWARD_CATEGORY_NOT_FOUND(BAD_REQUEST, "REWARD_CATEGORY000", "해당 경품 카테고리가 존재하지 않습니다."),

    /**
     * 알림
     */
    NOTIFICATION_NOT_FOUND(BAD_REQUEST, "NOTIFICATION000", "해당 알림이 존재하지 않습니다."),
    INVALID_RECEIVER(FORBIDDEN, "NOTIFICATION001", "자신의 알림만 읽을 수 있습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
