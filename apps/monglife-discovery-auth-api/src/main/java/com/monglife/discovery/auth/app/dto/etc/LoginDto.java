package com.monglife.discovery.auth.app.dto.etc;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginDto {

    private Long accountId;

    private String accessToken;

    private String refreshToken;
}
