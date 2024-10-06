package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.Account;

import java.util.Optional;

public interface CustomAccountRepository {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByAccountId(Long accountId);
}
