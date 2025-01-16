package com.monglife.discovery.app.auth.account.entity;

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
@RedisHash("monglife_token")
public class TokenEntity {

    @Id
    private String refreshToken;

    @Indexed
    private String deviceId;

    @Indexed
    private Long accountId;

    private String appPackageName;

    private String buildVersion;

    private LocalDateTime createdAt;

    @TimeToLive
    private Long expiration;
}
