package com.formssafe.global.error.type;

import com.formssafe.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class FormssafeException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public FormssafeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public FormssafeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public FormssafeException(ErrorCode errorCode, String message, Throwable cause) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.initCause(cause);
    }
}
