package com.monglife.discovery.auth.global.enums;

import com.monglife.core.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "AUTH-100", "access token expired."),
    NOT_FOUND_SESSION(HttpStatus.NOT_FOUND.value(), "AUTH-101", "not found session."),
    PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "AUTH-102", "passport generate fail."),
    ;

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
