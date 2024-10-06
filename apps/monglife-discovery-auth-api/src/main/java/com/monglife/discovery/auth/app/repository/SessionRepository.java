package com.monglife.discovery.auth.app.repository;

import com.monglife.discovery.auth.app.domain.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, String> {

    Optional<Session> findByDeviceIdAndAccountId(String deviceId, Long accountId);
}
