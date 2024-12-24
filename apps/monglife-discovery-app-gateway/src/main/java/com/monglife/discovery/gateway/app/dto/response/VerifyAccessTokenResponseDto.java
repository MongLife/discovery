package com.monglife.discovery.gateway.app.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class VerifyAccessTokenResponseDto {

    private String accessToken;

    @Builder
    public VerifyAccessTokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}