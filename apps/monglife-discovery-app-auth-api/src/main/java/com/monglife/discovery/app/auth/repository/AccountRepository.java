package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.AccountEntity;
import com.monglife.discovery.app.auth.repositoryCustom.AccountCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>, AccountCustomRepository {
}
