package com.monglife.discovery.auth.global.exception;

import com.monglife.core.enums.error.ErrorCode;
import com.monglife.core.exception.ErrorException;

public class NotFoundException extends ErrorException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(Throwable e) {
        super(e);
    }

    public NotFoundException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
