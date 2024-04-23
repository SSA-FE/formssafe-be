package com.formssafe.global.error.type;

import com.formssafe.global.error.ErrorCode;

public class DataNotFoundException extends BusinessException {

    public DataNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public DataNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
