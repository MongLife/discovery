package com.monglife.discovery.gateway.business.dto.res;

import lombok.Builder;

@Builder
public record ValidationAccessTokenResDto(
        String accessToken
) {
}