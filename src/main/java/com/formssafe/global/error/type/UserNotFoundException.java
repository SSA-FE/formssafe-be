package com.formssafe.global.error.type;

public class UserNotFoundException extends FormssafeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
