package com.monglife.discovery.gateway.app.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ValidationAccessTokenResponseDto {

    private String accessToken;
}