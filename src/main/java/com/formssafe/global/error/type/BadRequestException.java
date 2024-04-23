package com.formssafe.global.error.type;

public class BadRequestException extends FormssafeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
