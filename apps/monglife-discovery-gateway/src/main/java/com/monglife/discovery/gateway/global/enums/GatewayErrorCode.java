package com.monglife.discovery.gateway.global.enums;

import com.monglife.core.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GatewayErrorCode implements ErrorCode {
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "GATEWAY-100", "not found access token."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "GATEWAY-101", "access token expired."),
    PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GATEWAY-102", "passport generate fail."),
    PASSPORT_PARSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GATEWAY-103", "passport generate fail."),
    CONNECT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "GATEWAY-104", "connect service fail.")
    ;

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
