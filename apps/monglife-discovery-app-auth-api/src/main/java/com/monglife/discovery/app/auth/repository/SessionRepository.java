package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, String> {

    Optional<Session> findByDeviceIdAndAccountId(String deviceId, Long accountId);
}
