package com.monglife.discovery.app.auth.service;

import com.monglife.discovery.app.auth.dto.etc.ValidationAccessTokenDto;
import com.monglife.discovery.app.auth.repository.AccountLogRepository;
import com.monglife.discovery.app.auth.dto.etc.LoginDto;
import com.monglife.discovery.app.auth.dto.etc.ReissueDto;
import com.monglife.discovery.app.auth.domain.Account;
import com.monglife.discovery.app.auth.domain.AccountLog;
import com.monglife.discovery.app.auth.domain.Session;
import com.monglife.discovery.app.auth.repository.AccountRepository;
import com.monglife.discovery.app.auth.repository.SessionRepository;
import com.monglife.discovery.app.auth.global.enums.AuthErrorCode;
import com.monglife.discovery.app.auth.global.exception.ExpiredException;
import com.monglife.discovery.app.auth.global.exception.NotFoundException;
import com.monglife.discovery.app.auth.global.provider.AuthorizationTokenProvider;
import com.monglife.discovery.app.auth.global.enums.RoleCode;
import com.monglife.core.vo.passport.PassportDataAccountVo;
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

    private final AuthorizationTokenProvider tokenProvider;

    @Transactional
    public LoginDto login(String deviceId, String email, String name) {

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

        String accessToken = tokenProvider.generateAccessToken(account.getAccountId(), deviceId);

        // 새로운 세션 등록
        sessionRepository.save(Session.builder()
                .refreshToken(refreshToken)
                .deviceId(deviceId)
                .accountId(account.getAccountId())
                .createdAt(LocalDateTime.now())
                .expiration(refreshTokenExpiration)
                .build());

        // 로그인 로그 등록
        AccountLog accountLog = accountLogRepository.findByAccountIdAndDeviceIdAndLoginAt(account.getAccountId(), deviceId, LocalDate.now())
                .orElseGet(() -> accountLogRepository.save(AccountLog.builder()
                        .accountId(account.getAccountId())
                        .deviceId(deviceId)
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
    public Long logout(String refreshToken) {

        Session session = sessionRepository.findById(refreshToken)
                .orElseThrow(() -> new NotFoundException(AuthErrorCode.NOT_FOUND_SESSION));

        // 존재하는 세션 삭제
        sessionRepository.deleteById(session.getRefreshToken());

        return session.getAccountId();
    }

    @Transactional
    public ReissueDto reissue(String refreshToken) {

        Session session = sessionRepository.findById(refreshToken)
                .orElseThrow(() -> new NotFoundException(AuthErrorCode.NOT_FOUND_SESSION));

        // 존재하는 세션 삭제
        sessionRepository.deleteById(session.getRefreshToken());

        // AccessToken 및 RefreshToken 발급
        Long refreshTokenExpiration = tokenProvider.getRefreshTokenExpiration();

        refreshToken = tokenProvider.generateRefreshToken();

        String accessToken = tokenProvider.generateAccessToken(session.getAccountId(), session.getDeviceId());

        // 새로운 세션 등록
        sessionRepository.save(Session.builder()
                .refreshToken(refreshToken)
                .deviceId(session.getDeviceId())
                .accountId(session.getAccountId())
                .createdAt(LocalDateTime.now())
                .expiration(refreshTokenExpiration)
                .build());

        return ReissueDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public ValidationAccessTokenDto validationAccessToken(String accessToken) {

        if (tokenProvider.isTokenExpired(accessToken)) {
            throw new ExpiredException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        }

        return ValidationAccessTokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public PassportDataAccountVo findPassportDataAccount(String accessToken) {

        if (tokenProvider.isTokenExpired(accessToken)) {
            throw new ExpiredException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        }

        Long accountId = tokenProvider.getMemberId(accessToken)
                .orElseThrow(() -> new ExpiredException(AuthErrorCode.ACCESS_TOKEN_EXPIRED));

        String deviceId = tokenProvider.getDeviceId(accessToken)
                .orElseThrow(() -> new ExpiredException(AuthErrorCode.ACCESS_TOKEN_EXPIRED));

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundException(AuthErrorCode.PASSPORT_GENERATE_FAIL));

        return PassportDataAccountVo.builder()
                .accountId(accountId)
                .deviceId(deviceId)
                .email(account.getEmail())
                .name(account.getName())
                .role(account.getRole())
                .build();
    }
}
