package com.monglife.discovery.auth.app.dto.req;

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