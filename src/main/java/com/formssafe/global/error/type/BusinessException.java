package com.formssafe.global.error.type;

import com.formssafe.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends FormssafeException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}