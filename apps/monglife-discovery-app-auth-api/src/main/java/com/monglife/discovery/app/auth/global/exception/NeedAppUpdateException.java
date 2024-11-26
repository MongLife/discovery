package com.monglife.discovery.app.auth.global.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.discovery.app.auth.global.enums.AuthResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NeedAppUpdateException extends ErrorException {

    public NeedAppUpdateException() {
        this.response = AuthResponse.AUTH_NEED_UPDATE_APP_VERSION;
        this.result = Collections.emptyMap();
    }
}
