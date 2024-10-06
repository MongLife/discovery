package com.monglife.discovery.app.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReissueReqDto {

    @NotEmpty
    @NotBlank
    private String refreshToken;
}