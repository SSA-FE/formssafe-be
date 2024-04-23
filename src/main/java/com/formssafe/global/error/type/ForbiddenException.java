package com.formssafe.global.error.type;

public class ForbiddenException extends FormssafeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}