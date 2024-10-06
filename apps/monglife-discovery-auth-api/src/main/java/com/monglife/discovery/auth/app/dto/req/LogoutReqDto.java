package com.monglife.discovery.auth.app.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LogoutReqDto(
        @NotEmpty
        @NotBlank
        String refreshToken
) {}
