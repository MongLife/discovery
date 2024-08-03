package com.monglife.discovery.gateway.global.exception.error;

import com.monglife.core.code.ErrorCode;
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
