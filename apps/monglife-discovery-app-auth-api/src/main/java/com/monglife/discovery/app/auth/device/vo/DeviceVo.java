package com.monglife.discovery.app.auth.device.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeviceVo {

    private final String deviceId;

    private final String deviceName;

    private final String fcmToken;

    @Builder
    public DeviceVo(String deviceId, String deviceName, String fcmToken) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.fcmToken = fcmToken;
    }
}
