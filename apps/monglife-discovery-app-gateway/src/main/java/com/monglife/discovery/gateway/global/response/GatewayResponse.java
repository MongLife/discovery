package com.monglife.discovery.gateway.global.response;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum GatewayResponse implements Response {

    DISCOVERY_GATEWAY_ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED.value(), "DISCOVERY-GATEWAY-100", "access token 이 없습니다."),
    DISCOVERY_GATEWAY_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "DISCOVERY-GATEWAY-101", "만료된 access token 입니다."),
    DISCOVERY_GATEWAY_ACCESS_UNAUTHORIZATION_PATH(HttpStatus.UNAUTHORIZED.value(), "DISCOVERY-GATEWAY-102", "권한이 없습니다."),
    DISCOVERY_GATEWAY_PASSPORT_GENERATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DISCOVERY-GATEWAY-103", "passport 생성에 실패했습니다."),
    DISCOVERY_GATEWAY_PASSPORT_PARSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DISCOVERY-GATEWAY-104", "passport 파싱에 실패했습니다."),
    DISCOVERY_GATEWAY_CONNECT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DISCOVERY-GATEWAY-105", "서비스에 접근할 수 없습니다.")
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;

    @Override
    public ResponseDto<Map<String, Object>> toResponseDto() {
        return new ResponseDto<>(code, message, httpStatus, Collections.emptyMap());
    }

    @Override
    public <T> ResponseDto<T> toResponseDto(T result) {
        return new ResponseDto<>(code, message, httpStatus, result);
    }
}
