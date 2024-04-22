package com.formssafe.global.error.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    @Schema(description = "에러 코드")
    private final int code;
    @Schema(description = "에러 메시지")
    private final String message;

    private ExceptionResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ExceptionResponse of(int code, String message) {
        return new ExceptionResponse(code, message);
    }
}
