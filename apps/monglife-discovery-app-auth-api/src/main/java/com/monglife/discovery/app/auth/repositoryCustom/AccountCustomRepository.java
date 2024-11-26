package com.monglife.discovery.app.auth.repositoryCustom;

import com.monglife.discovery.app.auth.domain.AccountEntity;

import java.util.Optional;

public interface AccountCustomRepository {

    Optional<AccountEntity> findByEmail(String email);

    Optional<AccountEntity> findByAccountId(Long accountId);
}
