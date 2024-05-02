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
    USER_ALREADY_JOIN(BAD_REQUEST, "USER002", "이미 회원가입 하셨습니다."),
    USER_NICKNAME_DUPLICATE(BAD_REQUEST, "USER003", "중복된 닉네임이 존재합니다."),

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

    /**
     * Activity
     */
    NO_SUBMISSION_PARTICIPATED(BAD_REQUEST, "ACTIVITY000", "해당 설문에 대한 응답이 존재하지 않습니다."),

    /**
     * Submission
     */
    UNSUPPORTED_SUBMISSION_RESPONSE_TYPE(BAD_REQUEST, "SUBMISSION000", "올바르지 않은 submission type입니다."),

    /**
     * Result
     */
    EXCEL_FILE_CREATE_ERROR(INTERNAL_SERVER_ERROR, "RESULT000", "엑셀 파일을 생성하는데 실패하였습니다."),
    INVALID_AUTHOR_TO_CHECK_RESULT(FORBIDDEN, "RESULT001", "자신이 작성한 form의 결과만 확인 가능합니다."),
    /**
     * Content
     */
    INVALID_OPTION(BAD_REQUEST, "CONTENT000", "유효하지 않은 Content Type 입력입니다."),
    OBJECTIVE_QUESTION_REQUIRED_AT_LEAST_ONE_OPTION(BAD_REQUEST, "CONTENT001", "객관식 질문에는 적어도 한개 이상의 보기가 필요합니다."),
    QUESTION_DTO_CONVERT_ERROR(BAD_REQUEST, "CONTENT002", "Question 엔티티를 DTO로 변환할 수 없습니다."),
    UNEXPECTED_CONTENT_TYPE(BAD_REQUEST, "CONTENT003", "올바르지 않은 Content type입니다.");
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
