package com.monglife.discovery.app.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReissueRequestDto {

    @NotEmpty
    @NotBlank
    private String refreshToken;
}