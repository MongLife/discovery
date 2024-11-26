package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.LoginHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryCustomRepository extends JpaRepository<LoginHistoryEntity, Long>, com.monglife.discovery.app.auth.repositoryCustom.LoginHistoryCustomRepository {
}
