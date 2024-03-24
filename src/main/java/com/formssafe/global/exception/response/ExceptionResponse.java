package com.formssafe.global.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExceptionResponse {
    @Schema(description = "코드")
    private final int status;
    @Schema(description = "메시지")
    private final String message;

    private ExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ExceptionResponse of(int status, String message) {
        return new ExceptionResponse(status, message);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
