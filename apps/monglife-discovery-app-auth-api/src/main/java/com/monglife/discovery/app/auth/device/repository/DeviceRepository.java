package com.monglife.discovery.app.auth.device.repository;

import com.monglife.discovery.app.auth.device.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    Optional<DeviceEntity> findByDeviceId(String deviceId);

    List<DeviceEntity> findByAccountAccountId(Long accountId);

    Optional<DeviceEntity> findByAccountAccountIdAndDeviceId(Long accountId, String deviceId);
}
