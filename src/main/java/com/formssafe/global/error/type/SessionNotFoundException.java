package com.formssafe.global.error.type;

import com.formssafe.global.error.ErrorCode;

public class SessionNotFoundException extends FormssafeException {

    public SessionNotFoundException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public SessionNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public SessionNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
