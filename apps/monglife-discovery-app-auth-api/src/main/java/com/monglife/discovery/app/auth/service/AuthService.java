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
import com.monglife.discovery.app.auth.dto.etc.VerifyAccessTokenDto;
import com.monglife.discovery.app.auth.global.exception.*;
import com.monglife.discovery.app.auth.global.provider.AuthorizationTokenProvider;
import com.monglife.discovery.app.auth.repository.AccountRepository;
import com.monglife.discovery.app.auth.repository.AppVersionRepository;
import com.monglife.discovery.app.auth.repository.LoginHistoryRepository;
import com.monglife.discovery.app.auth.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;

    private final LoginHistoryRepository loginHistoryRepository;

    private final SessionRepository sessionRepository;

    private final AppVersionRepository appVersionRepository;

    private final AuthorizationTokenProvider tokenProvider;

    @Transactional
    public void join(String email, String name, String socialAccountId) {

        AccountEntity newAccountEntity = AccountEntity.builder()
                .email(email)
                .name(name)
                .socialAccountId(socialAccountId)
                .role(RoleCode.NORMAL.getRole())
                .build();

        accountRepository.save(newAccountEntity);
    }

    @Transactional
    public LoginDto oldLogin(String deviceId, String email) {

        AccountEntity accountEntity = accountRepository.findByEmail(email)
                .orElseThrow(AccountNotFoundException::new);

        String refreshToken = tokenProvider.generateRefreshToken();

        String accessToken = tokenProvider.generateAccessToken(accountEntity.getAccountId(), deviceId);

        return  LoginDto.builder()
                .accountId(accountEntity.getAccountId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public LoginDto login(String deviceId, String socialAccountId, String email, String appPackageName, String deviceName, String buildVersion) {

        AppVersionEntity appVersionEntity = appVersionRepository.findByAppPackageNameAndBuildVersion(appPackageName, buildVersion)
                .orElseThrow(() -> new AppVersionNotFoundException(appPackageName, buildVersion));

        // 앱 업데이트 여부 확인
        if (appVersionEntity.getMustUpdate()) {
            throw new NeedAppUpdateException();
        }

        // 회원 조회
        AccountEntity accountEntity = accountRepository.findBySocialAccountId(socialAccountId)
                .orElseGet(() -> accountRepository.findByEmail(email)
                            .orElseThrow(AccountNotFoundException::new));

        // 소셜 로그인 id 업데이트 (이전 사용자)
        accountEntity.updateSocialAccountId(socialAccountId);

        // 존재하는 세션 삭제
        sessionRepository.findByDeviceIdAndAccountId(deviceId, accountEntity.getAccountId())
                .ifPresent(sessionEntity -> sessionRepository.deleteById(sessionEntity.getRefreshToken()));

        // AccessToken 및 RefreshToken 발급
        String refreshToken = tokenProvider.generateRefreshToken();

        String accessToken = tokenProvider.generateAccessToken(accountEntity.getAccountId(), deviceId, appPackageName, buildVersion);

        // 새로운 세션 등록
        Long refreshTokenExpiration = tokenProvider.getRefreshTokenExpiration();

        sessionRepository.save(SessionEntity.builder()
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .accountId(accountEntity.getAccountId())
                .appPackageName(appPackageName)
                .buildVersion(buildVersion)
                .createdAt(LocalDateTime.now())
                .expiration(refreshTokenExpiration)
                .build());

        // 로그인 로그 등록
        LoginHistoryEntity loginHistoryEntity = loginHistoryRepository.findByAccountIdAndDeviceIdAndLoginAt(accountEntity.getAccountId(), deviceId, LocalDate.now())
                .orElseGet(() -> loginHistoryRepository.save(LoginHistoryEntity.builder()
                        .accountId(accountEntity.getAccountId())
                        .deviceId(deviceId)
                        .appPackageName(appPackageName)
                        .deviceName(deviceName)
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
                sessionEntity.getAppPackageName(),
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
    public VerifyAccessTokenDto verifyAccessToken(String accessToken) {

        if (tokenProvider.isTokenExpired(accessToken)) {
            throw new TokenExpiredException(accessToken);
        }

        return VerifyAccessTokenDto.builder()
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

        String appPackageName = tokenProvider.getAppPackageName(accessToken)
                .orElseThrow(() -> new TokenExpiredException(accessToken));

        String buildVersion = tokenProvider.getBuildVersion(accessToken)
                .orElseThrow(() -> new TokenExpiredException(accessToken));

        return PassportDataAppVersionVo.builder()
                .appPackageName(appPackageName)
                .buildVersion(buildVersion)
                .build();
    }
}
