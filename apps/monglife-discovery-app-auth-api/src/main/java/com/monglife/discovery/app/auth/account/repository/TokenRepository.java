package com.monglife.discovery.app.auth.account.repository;

import com.monglife.discovery.app.auth.account.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<TokenEntity, String> {

    Optional<TokenEntity> findByDeviceIdAndAccountId(String deviceId, Long accountId);
}
