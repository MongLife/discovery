package com.monglife.discovery.auth.app.dto.res;

import lombok.Builder;

@Builder
public record LoginResDto(
        Long accountId,
        String accessToken,
        String refreshToken
) {
}
