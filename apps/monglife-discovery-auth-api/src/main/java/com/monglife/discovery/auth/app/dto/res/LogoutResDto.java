package com.monglife.discovery.auth.app.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LogoutResDto {

    private Long accountId;
}
