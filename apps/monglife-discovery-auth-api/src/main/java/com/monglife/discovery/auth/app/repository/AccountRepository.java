package com.monglife.discovery.auth.app.repository;

import com.monglife.discovery.auth.app.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, CustomAccountRepository {
}
