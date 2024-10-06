package com.monglife.discovery.auth.app.controller;

import com.monglife.discovery.auth.app.dto.req.LoginReqDto;
import com.monglife.discovery.auth.app.dto.req.LogoutReqDto;
import com.monglife.discovery.auth.app.dto.req.ReissueReqDto;
import com.monglife.discovery.auth.app.dto.res.LoginResDto;
import com.monglife.discovery.auth.app.dto.res.LogoutResDto;
import com.monglife.discovery.auth.app.dto.res.ReissueResDto;
import com.monglife.discovery.auth.app.dto.res.ValidationAccessTokenResDto;
import com.monglife.discovery.auth.app.service.AuthService;
import com.monglife.discovery.auth.app.dto.etc.LoginDto;
import com.monglife.discovery.auth.app.dto.etc.ReissueDto;
import com.monglife.discovery.auth.app.dto.etc.ValidationAccessTokenDto;
import com.monglife.core.vo.passport.PassportDataAccountVo;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody @Validated LoginReqDto loginReqDto) {

        LoginDto loginDto = authService.login(loginReqDto.getDeviceId(), loginReqDto.getEmail(), loginReqDto.getName());

        return ResponseEntity.ok().body(LoginResDto.builder()
                .accountId(loginDto.getAccountId())
                .accessToken(loginDto.getAccessToken())
                .refreshToken(loginDto.getRefreshToken())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResDto> logout(@RequestBody @Validated LogoutReqDto logoutReqDto) {

        Long accountId = authService.logout(logoutReqDto.getRefreshToken());

        return ResponseEntity.ok().body(LogoutResDto.builder()
                .accountId(accountId)
                .build());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResDto> reissue(@RequestBody @Validated ReissueReqDto reissueReqDto) {

        ReissueDto reissueDto = authService.reissue(reissueReqDto.getRefreshToken());

        return ResponseEntity.ok().body(ReissueResDto.builder()
                .accessToken(reissueDto.getAccessToken())
                .refreshToken(reissueDto.getRefreshToken())
                .build());
    }

    @GetMapping("/validation/accessToken")
    public ResponseEntity<ValidationAccessTokenResDto> validationAccessToken(@RequestParam("accessToken") @NotBlank String accessToken) {

        ValidationAccessTokenDto validationAccessTokenDto = authService.validationAccessToken(accessToken);

        return ResponseEntity.ok().body(ValidationAccessTokenResDto.builder()
                .accessToken(validationAccessTokenDto.getAccessToken())
                .build());
    }

    @GetMapping("/passport")
    public ResponseEntity<PassportDataAccountVo> passport(@RequestParam("accessToken") @NotBlank String accessToken) {

        PassportDataAccountVo passportAccountVo = authService.findPassportDataAccount(accessToken);

        return ResponseEntity.ok().body(PassportDataAccountVo.builder()
                .accountId(passportAccountVo.accountId())
                .deviceId(passportAccountVo.deviceId())
                .email(passportAccountVo.email())
                .name(passportAccountVo.name())
                .role(passportAccountVo.role())
                .build());
    }
}
