package com.monglife.discovery.app.auth.global.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.discovery.app.auth.global.enums.AuthResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AccountNotFoundException extends ErrorException {

    public AccountNotFoundException(Long accountId) {
        this.response = AuthResponse.AUTH_NOT_EXISTS_ACCOUNT;
        this.result = Collections.singletonMap("accountId", accountId);
    }
}
