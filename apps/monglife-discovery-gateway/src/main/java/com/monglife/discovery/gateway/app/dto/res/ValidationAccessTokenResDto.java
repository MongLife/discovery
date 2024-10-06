package com.monglife.discovery.gateway.app.dto.res;

import lombok.Builder;

@Builder
public record ValidationAccessTokenResDto(
        String accessToken
) {
}