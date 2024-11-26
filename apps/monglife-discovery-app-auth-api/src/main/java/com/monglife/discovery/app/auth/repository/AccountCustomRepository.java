package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountCustomRepository extends JpaRepository<AccountEntity, Long>, com.monglife.discovery.app.auth.repositoryCustom.AccountCustomRepository {
}
