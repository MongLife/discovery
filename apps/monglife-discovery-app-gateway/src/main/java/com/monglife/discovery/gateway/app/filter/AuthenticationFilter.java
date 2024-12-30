package com.monglife.discovery.gateway.app.filter;

import com.monglife.discovery.gateway.app.service.WebClientService;
import com.monglife.discovery.gateway.global.config.FilterConfig;
import com.monglife.discovery.gateway.global.exception.PassportGenerateException;
import com.monglife.discovery.gateway.global.exception.TokenNotFoundException;
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
                    .orElseThrow(TokenNotFoundException::new)
                    .substring(7);

            return webClientService.verityAccessToken(accessToken)
                    .onErrorMap(throwable -> {

                        log.info("{}", throwable.getMessage());

                        throw new PassportGenerateException(accessToken);
                    })
                    .flatMap(validationAccessTokenResDto -> {
                        if (config.isPreLogger()) log.info("[AuthorizationFilter] AccessToken: {}", accessToken);
                        return chain.filter(exchange);
                    });
        };
    }
}
