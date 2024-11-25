package com.monglife.discovery.app.auth.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginResponseDto {

    private Long accountId;

    private String accessToken;

    private String refreshToken;
}
