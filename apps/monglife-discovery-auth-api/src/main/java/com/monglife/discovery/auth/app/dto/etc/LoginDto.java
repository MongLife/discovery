package com.monglife.discovery.auth.app.dto.etc;

import lombok.Builder;

@Builder
public record LoginDto(
        Long accountId,
        String accessToken,
        String refreshToken
) {
}
