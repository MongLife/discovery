package com.monglife.discovery.app.auth.global.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.discovery.app.auth.global.response.AuthResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class TokenExpiredException extends ErrorException {

    public TokenExpiredException(String accessToken) {
        this.response = AuthResponse.AUTH_ACCESS_TOKEN_EXPIRED;
        this.result = Collections.singletonMap("accessToken", accessToken);
    }
}
