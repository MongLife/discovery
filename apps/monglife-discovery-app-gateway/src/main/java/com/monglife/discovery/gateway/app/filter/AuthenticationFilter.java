package com.monglife.discovery.gateway.app.filter;

import com.monglife.discovery.gateway.app.service.WebClientService;
import com.monglife.discovery.gateway.global.enums.GatewayErrorCode;
import com.monglife.discovery.gateway.global.exception.error.GenerateException;
import com.monglife.discovery.gateway.global.exception.error.NotFoundException;
import com.monglife.discovery.gateway.global.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<FilterConfig> {

    private final WebClientService webClientService;
    private final HttpUtils httpUtils;

    public AuthenticationFilter(WebClientService webClientService, HttpUtils httpUtils) {
        super(FilterConfig.class);
        this.webClientService = webClientService;
        this.httpUtils = httpUtils;
    }

    @Override
    public GatewayFilter apply(FilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = httpUtils.getHeader(request, "Authorization")
                    .orElseThrow(() -> new NotFoundException(GatewayErrorCode.ACCESS_TOKEN_NOT_FOUND))
                    .substring(7);

            return webClientService.validationAccessToken(accessToken)
                    .onErrorMap(throwable -> new GenerateException(GatewayErrorCode.ACCESS_TOKEN_EXPIRED))
                    .flatMap(validationAccessTokenResDto -> {

                        if (config.preLogger) {
                            log.info("[AuthorizationFilter] AccessToken: {}", accessToken);
                        }

                        return chain.filter(exchange);
                    });
        };
    }
}
