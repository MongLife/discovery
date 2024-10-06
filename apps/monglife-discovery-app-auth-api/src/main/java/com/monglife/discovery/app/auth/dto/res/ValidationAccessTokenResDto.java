package com.monglife.discovery.app.auth.dto.res;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ValidationAccessTokenResDto {

    private String accessToken;
}
