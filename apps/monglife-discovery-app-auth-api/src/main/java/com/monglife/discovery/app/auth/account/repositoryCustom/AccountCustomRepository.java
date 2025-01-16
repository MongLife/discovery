package com.monglife.discovery.app.auth.account.repositoryCustom;

import com.monglife.discovery.app.auth.account.entity.AccountEntity;

import java.util.Optional;

public interface AccountCustomRepository {

    Optional<AccountEntity> findByEmail(String email);

    Optional<AccountEntity> findBySocialAccountId(String socialAccountId);

    Optional<AccountEntity> findByAccountId(Long accountId);
}
