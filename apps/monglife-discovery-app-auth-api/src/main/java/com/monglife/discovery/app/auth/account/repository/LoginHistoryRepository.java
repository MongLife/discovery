package com.monglife.discovery.app.auth.account.repository;

import com.monglife.discovery.app.auth.account.entity.LoginHistoryEntity;
import com.monglife.discovery.app.auth.account.repositoryCustom.LoginHistoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long>, LoginHistoryCustomRepository {
}
