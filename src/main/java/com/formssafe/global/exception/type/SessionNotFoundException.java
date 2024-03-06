package com.formssafe.global.exception.type;

import java.util.Arrays;

public class SessionNotFoundException extends FormssafeException {

    public SessionNotFoundException(String message, Object... params) {
        super(message + Arrays.toString(params));
    }

    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
