package com.monglife.discovery.app.auth.account.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
