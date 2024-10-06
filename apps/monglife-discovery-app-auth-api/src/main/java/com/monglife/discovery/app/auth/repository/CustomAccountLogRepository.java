package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.AccountLog;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CustomAccountLogRepository {

    Optional<AccountLog> findByAccountIdAndDeviceIdAndLoginAt(@Param("accountId") Long accountId, @Param("deviceId") String deviceId, @Param("loginAt") LocalDate loginAt);
}
