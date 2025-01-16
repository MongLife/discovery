package com.monglife.discovery.app.auth.device.controller;


import com.monglife.core.dto.response.ResponseDto;
import com.monglife.discovery.app.auth.device.dto.request.CreateDeviceRequestDto;
import com.monglife.discovery.app.auth.device.dto.response.GetDeviceResponseDto;
import com.monglife.discovery.app.auth.device.service.DeviceService;
import com.monglife.discovery.app.auth.device.vo.DeviceVo;
import com.monglife.discovery.app.auth.global.enums.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * 플레이어 기기 등록
     * @param createDeviceRequestDto 걸음 수 Dto
     * @return 성공 응답
     */
    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> createDevice(@RequestBody CreateDeviceRequestDto createDeviceRequestDto) {

        String deviceId = createDeviceRequestDto.getDeviceId();
        String deviceName = createDeviceRequestDto.getDeviceName();
        String fcmToken = createDeviceRequestDto.getFcmToken();

        deviceService.createDevice(deviceId, deviceName, fcmToken);

        return ResponseEntity.ok(AuthResponse.DISCOVERY_DEVICE_CREATE_DEVICE.toResponseDto());
    }

    /**
     * 기기 조회
     * @param accountId 계정 ID
     * @return 계정 ID 에 연결된 기기 정보 목록
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<ResponseDto<List<GetDeviceResponseDto>>> getDevices(@PathVariable("accountId") Long accountId) {

        List<DeviceVo> deviceVos = deviceService.getDevices(accountId);

        List<GetDeviceResponseDto> getDeviceResponseDtos = deviceVos.stream()
                .map(deviceVo -> GetDeviceResponseDto.builder()
                        .deviceId(deviceVo.getDeviceId())
                        .deviceName(deviceVo.getDeviceName())
                        .fcmToken(deviceVo.getFcmToken())
                        .build())
                .toList();

        return ResponseEntity.ok(AuthResponse.DISCOVERY_DEVICE_GET_DEVICE.toResponseDto(getDeviceResponseDtos));
    }
}
