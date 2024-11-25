package com.monglife.discovery.app.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@RedisHash("session")
public class Session {

    @Id
    private String refreshToken;

    @Indexed
    private String deviceId;

    @Indexed
    private Long accountId;

    private String appCode;

    private String buildVersion;

    private LocalDateTime createdAt;

    @TimeToLive
    private Long expiration;
}
