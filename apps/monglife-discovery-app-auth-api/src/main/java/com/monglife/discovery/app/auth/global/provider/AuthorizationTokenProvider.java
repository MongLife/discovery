package com.monglife.discovery.app.auth.global.provider;

import com.monglife.discovery.app.auth.global.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationTokenProvider {

    @Value("${env.jwt.access-expiration}")
    private Long ACCESS_TOKEN_EXPIRED;

    @Value("${env.jwt.refresh-expiration}")
    private Long REFRESH_TOKEN_EXPIRED;

    private final JwtTokenUtil jwtTokenUtil;

    public Long getRefreshTokenExpiration() {
        return REFRESH_TOKEN_EXPIRED;
    }

    public Boolean isTokenExpired(String token) {

        try {
            Date expiration = jwtTokenUtil.extractAllClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException | SignatureException | IllegalArgumentException e) {
            return true;
        }
    }

    public String generateAccessToken(Long accountId, String deviceId, String appCode, String buildVersion) {

        Claims claims = Jwts.claims();
        claims.put("accountId", accountId);
        claims.put("deviceId", deviceId);
        claims.put("appCode", appCode);
        claims.put("buildVersion", buildVersion);

        return jwtTokenUtil.generateToken(claims, ACCESS_TOKEN_EXPIRED);
    }

    public String generateRefreshToken() {

        Claims claims = Jwts.claims();

        return jwtTokenUtil.generateToken(claims, REFRESH_TOKEN_EXPIRED);
    }

    public Optional<Long> getAccountId(String token) {

        if (isTokenExpired(token)) {
            return Optional.empty();
        }

        return Optional.ofNullable(jwtTokenUtil.extractAllClaims(token).get("accountId", Long.class));
    }

    public Optional<String> getDeviceId(String token) {

        if (isTokenExpired(token)) {
            return Optional.empty();
        }

        return Optional.ofNullable(jwtTokenUtil.extractAllClaims(token).get("deviceId", String.class));
    }

    public Optional<String> getAppCode(String token) {

        if (isTokenExpired(token)) {
            return Optional.empty();
        }

        return Optional.ofNullable(jwtTokenUtil.extractAllClaims(token).get("appCode", String.class));
    }

    public Optional<String> getBuildVersion(String token) {

        if (isTokenExpired(token)) {
            return Optional.empty();
        }

        return Optional.ofNullable(jwtTokenUtil.extractAllClaims(token).get("buildVersion", String.class));
    }
}