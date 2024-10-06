package com.monglife.discovery.auth.app.dto.res;

import lombok.Builder;

@Builder
public record ValidationAccessTokenResDto(
        String accessToken
) {
}