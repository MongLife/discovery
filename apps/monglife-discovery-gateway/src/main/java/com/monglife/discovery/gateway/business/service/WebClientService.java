package com.monglife.discovery.gateway.business.service;

import com.monglife.core.vo.passport.PassportDataAccountVo;
import com.monglife.discovery.gateway.business.dto.res.ValidationAccessTokenResDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {

    private final WebClient authWebClient;

    public WebClientService(@Qualifier("authWebClient") WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    public Mono<ValidationAccessTokenResDto> validationAccessToken(String accessToken) {

        String url = "/auth/validation/accessToken?accessToken=%s".formatted(accessToken);

        return authWebClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ValidationAccessTokenResDto.class);
    }

    public Mono<PassportDataAccountVo> getPassport(String accessToken) {

        String url = "/auth/passport?accessToken=%s".formatted(accessToken);

        return authWebClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(PassportDataAccountVo.class);
    }
}
