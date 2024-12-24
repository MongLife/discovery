package com.monglife.discovery.app.auth.controller;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.vo.passport.PassportDataAccountVo;
import com.monglife.core.vo.passport.PassportDataAppVersionVo;
import com.monglife.discovery.app.auth.dto.etc.LoginDto;
import com.monglife.discovery.app.auth.dto.etc.ReissueDto;
import com.monglife.discovery.app.auth.dto.etc.VerifyAccessTokenDto;
import com.monglife.discovery.app.auth.dto.request.*;
import com.monglife.discovery.app.auth.dto.response.*;
import com.monglife.discovery.app.auth.global.enums.AuthResponse;
import com.monglife.discovery.app.auth.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto<Map<String, Object>>> join(@RequestBody JoinRequestDto joinRequestDto) {

        String email = joinRequestDto.getEmail();
        String name = joinRequestDto.getName();
        String socialAccountId = joinRequestDto.getSocialAccountId();

        authService.join(email, name, socialAccountId);

        return ResponseEntity.ok().body(AuthResponse.DISCOVERY_AUTH_JOIN.toResponseDto());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {

        String deviceId = loginRequestDto.getDeviceId();
        String email = loginRequestDto.getEmail();
        String socialAccountId = loginRequestDto.getSocialAccountId();
        String appPackageName = loginRequestDto.getAppPackageName();
        String deviceName = loginRequestDto.getDeviceName();
        String buildVersion = loginRequestDto.getBuildVersion();

        LoginDto loginDto = authService.login(deviceId, socialAccountId, email, appPackageName, deviceName, buildVersion);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .accountId(loginDto.getAccountId())
                .accessToken(loginDto.getAccessToken())
                .refreshToken(loginDto.getRefreshToken())
                .build();

        return ResponseEntity.ok().body(AuthResponse.DISCOVERY_AUTH_LOGIN.toResponseDto(loginResponseDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Map<String, Object>>> logout(@RequestBody LogoutRequestDto logoutRequestDto) {

        String refreshToken = logoutRequestDto.getRefreshToken();

        authService.logout(refreshToken);

        return ResponseEntity.ok().body(AuthResponse.DISCOVERY_AUTH_LOGOUT.toResponseDto());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto<ReissueResponseDto>> reissue(@RequestBody ReissueRequestDto reissueRequestDto) {

        ReissueDto reissueDto = authService.reissue(reissueRequestDto.getRefreshToken());

        ReissueResponseDto reissueResponseDto = ReissueResponseDto.builder()
                .accessToken(reissueDto.getAccessToken())
                .refreshToken(reissueDto.getRefreshToken())
                .build();

        return ResponseEntity.ok().body(AuthResponse.DISCOVERY_AUTH_REISSUE.toResponseDto(reissueResponseDto));
    }

    @GetMapping("/verify/accessToken")
    public ResponseEntity<ResponseDto<ValidationAccessTokenResponseDto>> verifyAccessToken(@RequestParam("accessToken") @NotBlank String accessToken) {

        VerifyAccessTokenDto verifyAccessTokenDto = authService.verifyAccessToken(accessToken);

        ValidationAccessTokenResponseDto validationAccessTokenResponseDto = ValidationAccessTokenResponseDto.builder()
                .accessToken(verifyAccessTokenDto.getAccessToken())
                .build();

        return ResponseEntity.ok().body(AuthResponse.DISCOVERY_AUTH_VALIDATION_TOKEN.toResponseDto(validationAccessTokenResponseDto));
    }

    @GetMapping("/passport")
    public ResponseEntity<ResponseDto<PassportDataResponseDto>> getPassportData(@RequestParam("accessToken") @NotBlank String accessToken) {

        PassportDataAccountVo passportDataAccountVo = authService.getPassportDataAccount(accessToken);

        PassportDataAppVersionVo passportDataAppVersionVo = authService.getPassportDataAppVersion(accessToken);

        PassportDataResponseDto passportDataResponseDto = PassportDataResponseDto.builder()
                .passportDataAccountVo(passportDataAccountVo)
                .passportDataAppVersionVo(passportDataAppVersionVo)
                .build();

        return ResponseEntity.ok().body(AuthResponse.DISCOVERY_AUTH_GET_PASSPORT.toResponseDto(passportDataResponseDto));
    }
}
