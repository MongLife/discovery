package com.monglife.discovery.gateway.filter;

import com.monglife.core.vo.passport.PassportDataVo;
import com.monglife.core.vo.passport.PassportVo;
import com.monglife.discovery.gateway.business.service.WebClientService;
import com.monglife.discovery.gateway.global.code.error.GatewayErrorCode;
import com.monglife.discovery.gateway.global.exception.error.GenerateException;
import com.monglife.discovery.gateway.global.exception.error.NotFoundException;
import com.monglife.discovery.gateway.global.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component
public class GeneratePassportFilter extends AbstractGatewayFilterFactory<FilterConfig> {

    private final WebClientService webClientService;
    private final HttpUtils httpUtils;

    public GeneratePassportFilter(WebClientService webClientService, HttpUtils httpUtils) {
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

            return webClientService.getPassport(accessToken)
                    .onErrorMap(throwable -> new GenerateException(GatewayErrorCode.PASSPORT_GENERATE_FAIL))
                    .flatMap(passportDataAccountVo -> {

                        PassportVo passportVo = PassportVo.builder()
                                .data(PassportDataVo.builder()
                                        .account(passportDataAccountVo)
                                        .build())
                                .createdAt(LocalDateTime.now())
                                .build();

                        String passportJson = httpUtils.getJsonString(passportVo)
                                .orElseThrow(() -> new GenerateException(GatewayErrorCode.PASSPORT_PARSING_FAIL));

                        request.mutate().header("passport", URLEncoder.encode(passportJson, StandardCharsets.UTF_8)).build();

                        if (config.preLogger) {
                            log.info("[PassportFilter] Passport: {}", passportJson);
                        }

                        return chain.filter(exchange);
                    });
        };
    }
}
