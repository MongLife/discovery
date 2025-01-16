package com.monglife.discovery.app.auth.account.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.discovery.app.auth.global.enums.AuthResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AccountNotFoundException extends ErrorException {

    public AccountNotFoundException() {
        this.response = AuthResponse.DISCOVERY_AUTH_NOT_EXISTS_ACCOUNT;
        this.result = Collections.emptyMap();
    }

    public AccountNotFoundException(Long accountId) {
        this.response = AuthResponse.DISCOVERY_AUTH_NOT_EXISTS_ACCOUNT;
        this.result = Collections.singletonMap("accountId", accountId);
    }

    public AccountNotFoundException(String socialAccountId) {
        this.response = AuthResponse.DISCOVERY_AUTH_NOT_EXISTS_ACCOUNT;
        this.result = Collections.singletonMap("socialAccountId", socialAccountId);
    }
}
