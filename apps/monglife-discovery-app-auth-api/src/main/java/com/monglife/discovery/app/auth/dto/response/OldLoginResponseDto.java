package com.monglife.discovery.app.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OldLoginResponseDto {

    private Long accountId;

    private String accessToken;

    private String refreshToken;

    @Builder
    public OldLoginResponseDto(Long accountId, String accessToken, String refreshToken) {
        this.accountId = accountId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
