package com.monglife.discovery.auth.app.repository;

import com.monglife.discovery.auth.app.domain.AccountLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLogRepository extends JpaRepository<AccountLog, Long>, CustomAccountLogRepository {
}
