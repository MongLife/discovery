package com.monglife.discovery.gateway.global.exception.error;

import com.monglife.core.code.ErrorCode;
import com.monglife.core.exception.ErrorException;

public class GenerateException extends ErrorException {
    public GenerateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GenerateException(Throwable e) {
        super(e);
    }

    public GenerateException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
