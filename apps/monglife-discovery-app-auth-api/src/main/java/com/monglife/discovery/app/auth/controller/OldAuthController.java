package com.monglife.discovery.app.auth.controller;

import com.monglife.discovery.app.auth.dto.etc.LoginDto;
import com.monglife.discovery.app.auth.dto.request.OldLoginRequestDto;
import com.monglife.discovery.app.auth.dto.response.OldLoginResponseDto;
import com.monglife.discovery.app.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OldAuthController {

    private final AuthService authService;

    /**
     * v1 로그인 컨트롤러
     * @param oldLoginRequestDto 로그인 요청 Dto
     * @return 로그인 응답 Dto
     */
    @PostMapping("/login")
    public ResponseEntity<OldLoginResponseDto> oldLogin(@RequestBody @Validated OldLoginRequestDto oldLoginRequestDto) {

        String deviceId = oldLoginRequestDto.getDeviceId();
        String email = oldLoginRequestDto.getEmail();
        String name = oldLoginRequestDto.getName();

        LoginDto loginDto = authService.oldLogin(deviceId, email);

        return ResponseEntity.ok().body(OldLoginResponseDto.builder()
                .accountId(loginDto.getAccountId())
                .accessToken(loginDto.getAccessToken())
                .refreshToken(loginDto.getRefreshToken())
                .build());
    }
}
