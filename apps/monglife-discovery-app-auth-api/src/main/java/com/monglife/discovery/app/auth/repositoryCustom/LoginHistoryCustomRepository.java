package com.monglife.discovery.app.auth.repositoryCustom;

import com.monglife.discovery.app.auth.domain.LoginHistoryEntity;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface LoginHistoryCustomRepository {

    Optional<LoginHistoryEntity> findByAccountIdAndDeviceIdAndLoginAt(@Param("accountId") Long accountId, @Param("deviceId") String deviceId, @Param("loginAt") LocalDate loginAt);
}
