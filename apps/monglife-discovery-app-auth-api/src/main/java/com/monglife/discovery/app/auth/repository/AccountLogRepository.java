package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.AccountLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLogRepository extends JpaRepository<AccountLog, Long>, CustomAccountLogRepository {
}
