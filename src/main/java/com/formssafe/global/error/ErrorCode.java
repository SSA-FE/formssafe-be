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
    BAD_REQUEST_ERROR(BAD_REQUEST, "SYSTEM001", "유효하지 않은 요청입니다."),

    /**
     * 인증
     */
    SESSION_NOT_FOUND(UNAUTHORIZED, "AUTH000", "인증이 필요합니다."),

    /**
     * 소셜 로그인
     */
    UNSUPPORTED_OAUTH_TYPE(BAD_REQUEST, "OAUTH000", "지원하지 않는 소셜 로그인 타입입니다."),

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
    INVALID_FORM_AUTHOR(FORBIDDEN, "FORM001", "작성자가 아닙니다."),
    TEMP_FORM(BAD_REQUEST, "FORM002", "임시 설문입니다."),
    NOT_TEMP_FORM(BAD_REQUEST, "FORM003", "임시 설문이 아닙니다."),
    NOT_PROGRESS_FORM(BAD_REQUEST, "FORM004", "현재 진행 중인 설문이 아닙니다."),
    INVALID_AUTO_END_DATE(BAD_REQUEST, "FORM005", "자동 마감 시각은 현재 시각 5분 후부터 설정할 수 있습니다."),
    INVALID_PRIVACY_DISPOSAL_DATE(BAD_REQUEST, "FORM006", "개인 정보 폐기 시각은 마감 시각과 같거나 후여야 합니다."),
    INVALID_QUESTION_SIZE(BAD_REQUEST, "FORM007", "설문 질문 문항은 1개 이상 100개 이하여야 합니다."),
    INVALID_FORM_STATUS(BAD_REQUEST, "FORM008", "유효하지 않은 설문 상태입니다."),
    INVALID_FORM_TITLE_LENGTH(BAD_REQUEST, "FORM009", "설문 제목은 1자 이상 100자 이하여야 합니다."),
    INVALID_TOTAL_IMAGE_SIZE(BAD_REQUEST, "FORM010", "이미지 파일은 5개 이하로 첨부할 수 있습니다."),
    INVALID_FORM_DESCRIPTION_LENGTH(BAD_REQUEST, "FORM011", "설문 설명은 2000자 이하로 작성해야 합니다."),
    INVALID_TEMP_FORM_EXPECT_TIME(BAD_REQUEST, "FORM012", "임시 설문의 예상 소요 시간은 1440분 이하여야 합니다."),
    INVALID_FORM_EXPECT_TIME(BAD_REQUEST, "FORM013", "설문의 예상 소요 시간은 1분 이상 1440분 이하여야 합니다."),

    /**
     * 경품 카테고리
     */
    REWARD_CATEGORY_NOT_FOUND(BAD_REQUEST, "REWARD_CATEGORY000", "해당 경품 카테고리가 존재하지 않습니다."),
    INVALID_REWARD_NAME_LENGTH(BAD_REQUEST, "REWARD_CATEGORY001", "경품 이름은 1자 이상 200자 이하여야 합니다."),
    INVALID_REWARD_COUNT(BAD_REQUEST, "REWARD_CATEGORY002", "경품 수량은 1개 이상 1,000,000개 이하여야 합니다."),

    /**
     * 태그
     */
    TAG_NOT_FOUND(BAD_REQUEST, "TAG000", "해당 태그가 존재하지 않습니다."),
    INVALID_TOTAL_TAG_SIZE(BAD_REQUEST, "TAG001", "태그는 5개 이하로 등록할 수 있습니다."),
    INVALID_TAG_NAME_LENGTH(BAD_REQUEST, "TAG002", "태그 이름은 1자 이상 10자 이하여야 합니다."),

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
    ONLY_ONE_SUBMISSION_ALLOWED(BAD_REQUEST, "SUBMISSION001", "한 사용자가 하나의 설문에 하나의 응답만 작성 가능합니다."),
    NO_EXISTING_SUBMISSION_FOUND(BAD_REQUEST, "SUBMISSION002", "등록되어 있는 응답이 존재하지 않습니다."),
    NOT_TEMPORARY_SUBMISSION(BAD_REQUEST, "SUBMISSION003", "해당 응답은 완료된 응답입니다."),
    SUBMISSION_TYPE_MISMATCH(BAD_REQUEST, "SUBMISSION004", "질문 타입이 올바르지 않습니다."),
    ENTRY_SUBMITTED_EXCEEDS_QUESTIONS(BAD_REQUEST, "SUBMISSION005", "입력된 제출의 갯수가 질문의 문항 수보다 많습니다."),
    REQUIRED_QUESTIONS_UNANSWERED(BAD_REQUEST, "SUBMISSION006", "답변되지 않은 필수문항이 존재합니다."),
    FORM_STATUS_NOT_IN_PROGRESS(BAD_REQUEST, "SUBMISSION007", "참여하고자 하는 설문이 진행중이지 않습니다."),
    CANNOT_SUBMIT_FORM_YOU_CREATED(BAD_REQUEST, "SUBMISSION008", "자신이 작성한 설문에는 답변할 수 없습니다."),

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
    UNEXPECTED_CONTENT_TYPE(BAD_REQUEST, "CONTENT003", "올바르지 않은 Content type입니다."),
    INVALID_CONTENT_TITLE_LENGTH(BAD_REQUEST, "CONTENT004", "설문 문항 제목은 1자 이상 100자 이하여야 합니다."),
    INVALID_CONTENT_DESCRIPTION_LENGTH(BAD_REQUEST, "CONTENT005", "설문 문항 설명은 1000자 이하여야 합니다."),
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
