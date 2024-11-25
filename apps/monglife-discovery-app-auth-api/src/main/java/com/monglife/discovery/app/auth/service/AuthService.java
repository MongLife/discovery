package com.monglife.discovery.app.auth.service;

import com.monglife.core.enums.role.RoleCode;
import com.monglife.core.vo.passport.PassportDataAccountVo;
import com.monglife.core.vo.passport.PassportDataAppVersionVo;
import com.monglife.discovery.app.auth.domain.Account;
import com.monglife.discovery.app.auth.domain.AccountLog;
import com.monglife.discovery.app.auth.domain.AppVersion;
import com.monglife.discovery.app.auth.domain.Session;
import com.monglife.discovery.app.auth.dto.etc.LoginDto;
import com.monglife.discovery.app.auth.dto.etc.ReissueDto;
import com.monglife.discovery.app.auth.dto.etc.ValidationAccessTokenDto;
import com.monglife.discovery.app.auth.global.exception.*;
import com.monglife.discovery.app.auth.global.provider.AuthorizationTokenProvider;
import com.monglife.discovery.app.auth.repository.AccountLogRepository;
import com.monglife.discovery.app.auth.repository.AccountRepository;
import com.monglife.discovery.app.auth.repository.AppVersionRepository;
import com.monglife.discovery.app.auth.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final AccountLogRepository accountLogRepository;
    private final SessionRepository sessionRepository;

    private final AppVersionRepository appVersionRepository;

    private final AuthorizationTokenProvider tokenProvider;

    @Transactional
    public LoginDto login(String deviceId, String email, String name, String appCode, String buildVersion) {

        AppVersion appVersion = appVersionRepository.findByAppCodeAndBuildVersion(appCode, buildVersion)
                .orElseThrow(() -> new AppVersionNotFoundException(appCode, buildVersion));

        if (appVersion.getMustUpdate()) {
            throw new NeedAppUpdateException();
        }

        // 회원 조회 or 회원 생성
        Account account = accountRepository.findByEmail(email)
                .orElseGet(() -> {
                    Account newAccount = Account.builder()
                            .email(email)
                            .name(name)
                            .role(RoleCode.NORMAL.getRole())
                            .build();

                     return accountRepository.save(newAccount);
                });

        // 존재하는 세션 삭제
        sessionRepository.findByDeviceIdAndAccountId(deviceId, account.getAccountId())
                .ifPresent(session -> sessionRepository.deleteById(session.getRefreshToken()));

        // AccessToken 및 RefreshToken 발급
        Long refreshTokenExpiration = tokenProvider.getRefreshTokenExpiration();

        String refreshToken = tokenProvider.generateRefreshToken();

        String accessToken = tokenProvider.generateAccessToken(account.getAccountId(), deviceId, appCode, buildVersion);

        // 새로운 세션 등록
        sessionRepository.save(Session.builder()
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .accountId(account.getAccountId())
                .appCode(appCode)
                .buildVersion(buildVersion)
                .createdAt(LocalDateTime.now())
                .expiration(refreshTokenExpiration)
                .build());

        // 로그인 로그 등록
        AccountLog accountLog = accountLogRepository.findByAccountIdAndDeviceIdAndLoginAt(account.getAccountId(), deviceId, LocalDate.now())
                .orElseGet(() -> accountLogRepository.save(AccountLog.builder()
                        .accountId(account.getAccountId())
                        .deviceId(deviceId)
                        .appCode(appCode)
                        .buildVersion(buildVersion)
                        .build()));

        // 로그인 카운트 1 증가
        accountLog.increaseLoginCount();

        return  LoginDto.builder()
                .accountId(account.getAccountId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {

        Session session = sessionRepository.findById(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException(refreshToken));

        // 존재하는 세션 삭제
        sessionRepository.deleteById(session.getRefreshToken());
    }

    @Transactional
    public ReissueDto reissue(String refreshToken) {

        Session session = sessionRepository.findById(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException(refreshToken));

        // 존재하는 세션 삭제
        sessionRepository.deleteById(session.getRefreshToken());

        // AccessToken 및 RefreshToken 발급
        Long refreshTokenExpiration = tokenProvider.getRefreshTokenExpiration();

        String newRefreshToken = tokenProvider.generateRefreshToken();

        String newAccessToken = tokenProvider.generateAccessToken(
                session.getAccountId(),
                session.getDeviceId(),
                session.getAppCode(),
                session.getBuildVersion()
        );

        // 새로운 세션 등록
        sessionRepository.save(Session.builder()
                .refreshToken(newRefreshToken)
                .deviceId(session.getDeviceId())
                .accountId(session.getAccountId())
                .createdAt(LocalDateTime.now())
                .expiration(refreshTokenExpiration)
                .build());

        return ReissueDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional(readOnly = true)
    public ValidationAccessTokenDto validationAccessToken(String accessToken) {

        if (tokenProvider.isTokenExpired(accessToken)) {
            throw new TokenExpiredException(accessToken);
        }

        return ValidationAccessTokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    @Transactional(readOnly = true)
    public PassportDataAccountVo getPassportDataAccount(String accessToken) {

        if (tokenProvider.isTokenExpired(accessToken)) {
            throw new TokenExpiredException(accessToken);
        }

        Long accountId = tokenProvider.getAccountId(accessToken)
                .orElseThrow(() -> new TokenExpiredException(accessToken));

        String deviceId = tokenProvider.getDeviceId(accessToken)
                .orElseThrow(() -> new TokenExpiredException(accessToken));

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return PassportDataAccountVo.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .email(account.getEmail())
                .name(account.getName())
                .role(account.getRole())
                .build();
    }

    @Transactional(readOnly = true)
    public PassportDataAppVersionVo getPassportDataAppVersion(String accessToken) {

        if (tokenProvider.isTokenExpired(accessToken)) {
            throw new TokenExpiredException(accessToken);
        }

        String appCode = tokenProvider.getAppCode(accessToken)
                .orElseThrow(() -> new TokenExpiredException(accessToken));

        String buildVersion = tokenProvider.getBuildVersion(accessToken)
                .orElseThrow(() -> new TokenExpiredException(accessToken));

        return PassportDataAppVersionVo.builder()
                .appCode(appCode)
                .buildVersion(buildVersion)
                .build();
    }
}
