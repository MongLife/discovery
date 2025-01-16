package com.monglife.discovery.app.auth.device.entity;

import com.monglife.discovery.app.auth.account.entity.AccountEntity;
import com.monglife.discovery.app.auth.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({ AuditingEntityListener.class })
@Table(name = "monglife_device")
public class DeviceEntity extends BaseTimeEntity {

    @Id
    @Column(name = "device_id", unique = true)
    private String deviceId;

    @Column(name = "device_name")
    private String deviceName;

    @Setter
    @Column(name = "fcm_token")
    private String fcmToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Builder
    public DeviceEntity(String deviceId, String deviceName, String fcmToken) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.fcmToken = fcmToken;
    }

    public void connectAccount(AccountEntity accountEntity) {
        this.account = accountEntity;
    }

    public void disconnectAccount() {
        this.account = null;
    }
}
