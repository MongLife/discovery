package com.monglife.discovery.gateway.app.service;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.discovery.gateway.app.dto.response.PassportDataResponseDto;
import com.monglife.discovery.gateway.app.dto.response.ValidationAccessTokenResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WebClientService {

    private final WebClient authWebClient;

    public WebClientService(@Qualifier("authWebClient") WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    public Mono<ValidationAccessTokenResponseDto> validationAccessToken(String accessToken) {

        String url = "/auth/validation/accessToken?accessToken=%s".formatted(accessToken);

        return authWebClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ResponseDto<ValidationAccessTokenResponseDto>>() {})
                .map(ResponseDto::getResult);
    }

    public Mono<PassportDataResponseDto> getPassportData(String accessToken) {

        String url = "/auth/passport?accessToken=%s".formatted(accessToken);

        return authWebClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ResponseDto<PassportDataResponseDto>>() {})
                .map(ResponseDto::getResult);
    }
}
