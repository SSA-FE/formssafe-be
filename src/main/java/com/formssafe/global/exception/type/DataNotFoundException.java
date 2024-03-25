package com.formssafe.global.exception.type;

public class DataNotFoundException extends FormssafeException {

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
