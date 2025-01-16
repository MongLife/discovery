package com.monglife.discovery.app.auth.device.service;

import com.monglife.discovery.app.auth.account.entity.AccountEntity;
import com.monglife.discovery.app.auth.account.exception.AccountNotFoundException;
import com.monglife.discovery.app.auth.account.repository.AccountRepository;
import com.monglife.discovery.app.auth.device.entity.DeviceEntity;
import com.monglife.discovery.app.auth.device.repository.DeviceRepository;
import com.monglife.discovery.app.auth.device.vo.DeviceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final AccountRepository accountRepository;

    private final DeviceRepository deviceRepository;

    /**
     * 기기 등록
     * @param deviceId 기기 ID
     * @param fcmToken FCM 토큰
     */
    @Transactional
    public void createDevice(String deviceId, String deviceName, String fcmToken) {

        // 기존 기기 정보가 존재하지 않는 경우 등록
        DeviceEntity deviceEntity = deviceRepository.findByDeviceId(deviceId)
                .orElseGet(() -> deviceRepository.save(DeviceEntity.builder()
                        .deviceId(deviceId)
                        .deviceName(deviceName)
                        .fcmToken(fcmToken)
                        .build()));

        // FCM 토큰 갱신
        deviceEntity.setFcmToken(fcmToken);
    }


    /**
     * 기기 연결
     * @param accountId 계정 ID
     * @param deviceId 기기 ID
     */
    @Transactional
    public void connectDevice(Long accountId, String deviceId) {

        AccountEntity accountEntity = accountRepository.findByAccountId(accountId)
                .orElseThrow(AccountNotFoundException::new);

        deviceRepository.findByDeviceId(deviceId)
                .ifPresent(deviceEntity -> {
                    deviceEntity.connectAccount(accountEntity);
                });
    }

    /**
     * 기기 연결 해제
     * @param accountId 계정 ID
     * @param deviceId 기기 ID
     */
    @Transactional
    public void disconnectDevice(Long accountId, String deviceId) {

        deviceRepository.findByAccountAccountIdAndDeviceId(accountId, deviceId)
                .ifPresent(DeviceEntity::disconnectAccount);
    }

    /**
     * 기기 조회
     * @param accountId 계정 ID
     * @return 계정 ID 에 연결된 기기 목록 Vo
     */
    @Transactional
    public List<DeviceVo> getDevices(Long accountId) {

        List<DeviceEntity> deviceEntities = deviceRepository.findByAccountAccountId(accountId);

        return deviceEntities.stream()
                .map(deviceEntity -> DeviceVo.builder()
                        .deviceId(deviceEntity.getDeviceId())
                        .deviceName(deviceEntity.getDeviceName())
                        .fcmToken(deviceEntity.getFcmToken())
                        .build())
                .toList();
    }
}
