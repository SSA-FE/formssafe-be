package com.formssafe.global.error.type;

import com.formssafe.global.error.ErrorCode;
import java.util.Arrays;

public class SessionNotFoundException extends BusinessException {
    public SessionNotFoundException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }

    public SessionNotFoundException(ErrorCode errorCode, String message, Object... params) {
        super(errorCode, message + Arrays.toString(params));
    }

    public SessionNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
