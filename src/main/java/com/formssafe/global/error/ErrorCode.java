package com.formssafe.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /**
     * 디폴트 에러
     */
    SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "SYSTEM000", "서비스에 장애가 발생했습니다."),

    /**
     * 인증
     */
    UNAUTHORIZED_ACCESS(UNAUTHORIZED, "AUTH000", "권한이 없습니다"),

    /**
     * 소셜 로그인
     */
    UNSUPPORTED_OAUTH_TYPE(UNAUTHORIZED, "OAUTH000", "지원하지 않는 소셜 로그인 타입입니다."),

    /**
     * 사용자
     */
    USER_NOT_FOUND(BAD_REQUEST, "USER000", "해당 유저가 존재하지 않습니다."),
    INVALID_USER(FORBIDDEN, "USER001", "권한이 없습니다."),

    /**
     * 설문
     */
    FORM_NOT_FOUND(BAD_REQUEST, "FORM000", "해당 설문이 존재하지 않습니다."),
    INVALID_FORM_AUTHOR(FORBIDDEN, "FORM001", "작성자가 아닙니다."),
    TEMP_FORM(BAD_REQUEST, "FORM002", "임시 설문입니다."),
    NOT_TEMP_FORM(BAD_REQUEST, "FORM003", "임시 설문이 아닙니다."),
    NOT_PROGRESS_FORM(BAD_REQUEST, "FORM004", "현재 진행 중인 설문이 아닙니다."),
    INVALID_AUTO_END_DATE(BAD_REQUEST, "FORM005", "자동 마감 시각은 현재 시각 5분 후부터 설정할 수 있습니다."),
    INVALID_PRIVACY_DISPOSAL_DATE(BAD_REQUEST, "FORM006", "개인 정보 폐기 시각은 마감 시각과 같거나 후여야 합니다."),
    EMPTY_QUESTION(BAD_REQUEST, "FORM007", "설문에는 하나 이상의 설문 문항이 포함되어야 합니다."),
    INVALID_FORM_STATUS(BAD_REQUEST, "FORM008", "유효하지 않은 설문 상태입니다."),

    /**
     * 경품 카테고리
     */
    REWARD_CATEGORY_NOT_FOUND(BAD_REQUEST, "REWARD_CATEGORY000", "해당 경품 카테고리가 존재하지 않습니다."),

    /**
     * 태그
     */
    TAG_NOT_FOUND(BAD_REQUEST, "TAG000", "해당 태그가 존재하지 않습니다."),

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
}
