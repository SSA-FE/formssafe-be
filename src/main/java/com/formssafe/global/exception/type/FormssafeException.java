package com.formssafe.global.exception.type;

public class FormssafeException extends RuntimeException {

    public FormssafeException(String message) {
        super(message);
    }

    public FormssafeException(String message, Throwable cause) {
        super(message, cause);
    }
}
