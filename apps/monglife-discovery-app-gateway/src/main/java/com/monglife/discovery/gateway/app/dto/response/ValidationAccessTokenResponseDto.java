package com.monglife.discovery.gateway.app.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ValidationAccessTokenResponseDto {

    private String accessToken;

    @Builder
    public ValidationAccessTokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}