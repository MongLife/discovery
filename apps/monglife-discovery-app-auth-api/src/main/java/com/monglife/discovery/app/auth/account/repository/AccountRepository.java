package com.monglife.discovery.app.auth.account.repository;

import com.monglife.discovery.app.auth.account.entity.AccountEntity;
import com.monglife.discovery.app.auth.account.repositoryCustom.AccountCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>, AccountCustomRepository {
}
