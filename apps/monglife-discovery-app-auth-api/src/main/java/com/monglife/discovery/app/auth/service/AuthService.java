package com.monglife.discovery.app.auth.service;

import com.monglife.core.enums.role.RoleCode;
import com.monglife.core.vo.passport.PassportDataAccountVo;
import com.monglife.core.vo.passport.PassportDataAppVersionVo;
import com.monglife.discovery.app.auth.domain.AccountEntity;
import com.monglife.discovery.app.auth.domain.AppVersionEntity;
import com.monglife.discovery.app.auth.domain.LoginHistoryEntity;
import com.monglife.discovery.app.auth.domain.SessionEntity;
import com.monglife.discovery.app.auth.dto.etc.LoginDto;
import com.monglife.discovery.app.auth.dto.etc.ReissueDto;
import com.monglife.discovery.app.auth.dto.etc.ValidationAccessTokenDto;
import com.monglife.discovery.app.auth.global.exception.*;
import com.monglife.discovery.app.auth.global.provider.AuthorizationTokenProvider;
import com.monglife.discovery.app.auth.repository.LoginHistoryCustomRepository;
import com.monglife.discovery.app.auth.repository.AccountCustomRepository;
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

    private final AccountCustomRepository accountRepository;
    private final LoginHistoryCustomRepository loginHistoryRepository;
    private final SessionRepository sessionRepository;

    private final AppVersionRepository appVersionRepository;

    private final AuthorizationTokenProvider tokenProvider;

    @Transactional
    public LoginDto login(String deviceId, String email, String name, String appCode, String buildVersion) {

        AppVersionEntity appVersionEntity = appVersionRepository.findByAppCodeAndBuildVersion(appCode, buildVersion)
                .orElseThrow(() -> new AppVersionNotFoundException(appCode, buildVersion));

        if (appVersionEntity.getMustUpdate()) {
            throw new NeedAppUpdateException();
        }

        // 회원 조회 or 회원 생성
        AccountEntity accountEntity = accountRepository.findByEmail(email)
                .orElseGet(() -> {
                    AccountEntity newAccountEntity = AccountEntity.builder()
                            .email(email)
                            .name(name)
                            .role(RoleCode.NORMAL.getRole())
                            .build();

                     return accountRepository.save(newAccountEntity);
                });

        // 존재하는 세션 삭제
        sessionRepository.findByDeviceIdAndAccountId(deviceId, accountEntity.getAccountId())
                .ifPresent(sessionEntity -> sessionRepository.deleteById(sessionEntity.getRefreshToken()));

        // AccessToken 및 RefreshToken 발급
        Long refreshTokenExpiration = tokenProvider.getRefreshTokenExpiration();

        String refreshToken = tokenProvider.generateRefreshToken();

        String accessToken = tokenProvider.generateAccessToken(accountEntity.getAccountId(), deviceId, appCode, buildVersion);

        // 새로운 세션 등록
        sessionRepository.save(SessionEntity.builder()
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .accountId(accountEntity.getAccountId())
                .appCode(appCode)
                .buildVersion(buildVersion)
                .createdAt(LocalDateTime.now())
                .expiration(refreshTokenExpiration)
                .build());

        // 로그인 로그 등록
        LoginHistoryEntity loginHistoryEntity = loginHistoryRepository.findByAccountIdAndDeviceIdAndLoginAt(accountEntity.getAccountId(), deviceId, LocalDate.now())
                .orElseGet(() -> loginHistoryRepository.save(LoginHistoryEntity.builder()
                        .accountId(accountEntity.getAccountId())
                        .deviceId(deviceId)
                        .appCode(appCode)
                        .buildVersion(buildVersion)
                        .build()));

        // 로그인 카운트 1 증가
        loginHistoryEntity.increaseLoginCount();

        return  LoginDto.builder()
                .accountId(accountEntity.getAccountId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {

        SessionEntity sessionEntity = sessionRepository.findById(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException(refreshToken));

        // 존재하는 세션 삭제
        sessionRepository.deleteById(sessionEntity.getRefreshToken());
    }

    @Transactional
    public ReissueDto reissue(String refreshToken) {

        SessionEntity sessionEntity = sessionRepository.findById(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException(refreshToken));

        // 존재하는 세션 삭제
        sessionRepository.deleteById(sessionEntity.getRefreshToken());

        // AccessToken 및 RefreshToken 발급
        Long refreshTokenExpiration = tokenProvider.getRefreshTokenExpiration();

        String newRefreshToken = tokenProvider.generateRefreshToken();

        String newAccessToken = tokenProvider.generateAccessToken(
                sessionEntity.getAccountId(),
                sessionEntity.getDeviceId(),
                sessionEntity.getAppCode(),
                sessionEntity.getBuildVersion()
        );

        // 새로운 세션 등록
        sessionRepository.save(SessionEntity.builder()
                .refreshToken(newRefreshToken)
                .deviceId(sessionEntity.getDeviceId())
                .accountId(sessionEntity.getAccountId())
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

        AccountEntity accountEntity = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        return PassportDataAccountVo.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .email(accountEntity.getEmail())
                .name(accountEntity.getName())
                .role(accountEntity.getRole())
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
