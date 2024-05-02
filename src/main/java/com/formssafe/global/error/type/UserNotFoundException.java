package com.formssafe.global.error.type;

import com.formssafe.global.error.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public UserNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UserNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
