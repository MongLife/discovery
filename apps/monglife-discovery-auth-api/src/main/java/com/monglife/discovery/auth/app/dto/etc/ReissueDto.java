package com.monglife.discovery.auth.app.dto.etc;

import lombok.Builder;

@Builder
public record ReissueDto(
        String accessToken,
        String refreshToken
) {
}
