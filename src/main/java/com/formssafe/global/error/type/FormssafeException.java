package com.formssafe.global.error.type;

public class FormssafeException extends RuntimeException {

    public FormssafeException(String message) {
        super(message);
    }

    public FormssafeException(String message, Throwable cause) {
        super(message, cause);
    }
}
