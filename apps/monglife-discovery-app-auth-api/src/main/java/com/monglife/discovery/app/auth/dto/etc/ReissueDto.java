package com.monglife.discovery.app.auth.dto.etc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReissueDto {

    private String accessToken;

    private String refreshToken;
}
