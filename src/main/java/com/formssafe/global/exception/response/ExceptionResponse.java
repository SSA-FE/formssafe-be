package com.formssafe.global.exception.response;

public class ExceptionResponse {
    private final int status;
    private final String message;

    private ExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ExceptionResponse of(int status, String message) {
        return new ExceptionResponse(status, message);
    }

    public int status() {
        return status;
    }

    public String message() {
        return message;
    }
}
