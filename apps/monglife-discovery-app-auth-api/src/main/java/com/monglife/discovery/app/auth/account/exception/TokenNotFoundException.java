package com.monglife.discovery.app.auth.account.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.discovery.app.auth.global.enums.AuthResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class TokenNotFoundException extends ErrorException {

    public TokenNotFoundException(String refreshToken) {
        this.response = AuthResponse.DISCOVERY_AUTH_NOT_EXISTS_SESSION;
        this.result = Collections.singletonMap("refreshToken", refreshToken);
    }
}
