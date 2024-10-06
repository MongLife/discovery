package com.monglife.discovery.auth.app.dto.etc;

import lombok.Builder;

@Builder
public record ValidationAccessTokenDto(
        String accessToken
) {
}
