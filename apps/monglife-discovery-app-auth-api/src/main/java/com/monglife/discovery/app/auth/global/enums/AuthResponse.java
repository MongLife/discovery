package com.monglife.discovery.app.auth.global.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AuthResponse implements Response {

    DISCOVERY_AUTH_JOIN(HttpStatus.OK.value(), "DISCOVERY-AUTH-000", "회원가입에 성공하였습니다."),
    DISCOVERY_AUTH_LOGIN(HttpStatus.OK.value(), "DISCOVERY-AUTH-001", "로그인에 성공하였습니다."),
    DISCOVERY_AUTH_LOGOUT(HttpStatus.OK.value(), "DISCOVERY-AUTH-002", "로그아웃에 성공하였습니다."),
    DISCOVERY_AUTH_REISSUE(HttpStatus.OK.value(), "DISCOVERY-AUTH-003", "토큰 재발급에 성공하였습니다."),
    DISCOVERY_AUTH_VALIDATION_TOKEN(HttpStatus.OK.value(), "DISCOVERY-AUTH-004", "토큰 유효성 체크에 성공하였습니다."),
    DISCOVERY_AUTH_GET_PASSPORT(HttpStatus.OK.value(), "DISCOVERY-AUTH-005", "패스포트 발급에 성공하였습니다."),

    DISCOVERY_AUTH_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "DISCOVERY-AUTH-100", "만료된 토큰입니다."),
    DISCOVERY_AUTH_NOT_EXISTS_SESSION(HttpStatus.UNAUTHORIZED.value(), "DISCOVERY-AUTH-101", "새션이 존재하지 않습니다."),
    DISCOVERY_AUTH_NOT_EXISTS_ACCOUNT(HttpStatus.NOT_FOUND.value(), "DISCOVERY-AUTH-102", "계정이 존재하지 않습니다."),
    DISCOVERY_AUTH_NOT_EXISTS_APP_VERSION(HttpStatus.NOT_ACCEPTABLE.value(), "DISCOVERY-AUTH-103", "앱 버전이 존재하지 않습니다."),
    DISCOVERY_AUTH_NEED_UPDATE_APP_VERSION(HttpStatus.NOT_ACCEPTABLE.value(), "DISCOVERY-AUTH-104", "앱 업데이트가 필요합니다."),
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
