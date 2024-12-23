package com.monglife.discovery.app.auth.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidationAccessTokenDto {

    private String accessToken;

    @Builder
    public ValidationAccessTokenDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
