package com.monglife.discovery.auth.app.dto.etc;

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
