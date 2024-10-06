package com.monglife.discovery.app.auth.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ValidationAccessTokenDto {

    private String accessToken;
}
