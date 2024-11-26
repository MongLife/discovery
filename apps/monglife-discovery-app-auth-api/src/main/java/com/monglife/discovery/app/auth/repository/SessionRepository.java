package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.SessionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<SessionEntity, String> {

    Optional<SessionEntity> findByDeviceIdAndAccountId(String deviceId, Long accountId);
}
