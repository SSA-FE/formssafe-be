package com.formssafe.global.error.type;

import com.formssafe.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.initCause(cause);
    }
}