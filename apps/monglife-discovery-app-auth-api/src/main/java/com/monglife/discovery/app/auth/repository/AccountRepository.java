package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, CustomAccountRepository {
}
