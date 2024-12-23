package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.LoginHistoryEntity;
import com.monglife.discovery.app.auth.repositoryCustom.LoginHistoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long>, LoginHistoryCustomRepository {
}
