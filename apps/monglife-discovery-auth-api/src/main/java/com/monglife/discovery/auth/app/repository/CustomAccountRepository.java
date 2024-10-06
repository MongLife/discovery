package com.monglife.discovery.auth.app.repository;

import com.monglife.discovery.auth.app.domain.Account;

import java.util.Optional;

public interface CustomAccountRepository {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByAccountId(Long accountId);
}
