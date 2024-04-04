package com.formssafe.global.exception.type;

public class UserNotFoundException extends FormssafeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
