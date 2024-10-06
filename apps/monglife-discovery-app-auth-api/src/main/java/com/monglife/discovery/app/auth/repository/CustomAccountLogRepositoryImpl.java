package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.AccountLog;
import com.monglife.discovery.app.auth.domain.QAccountLog;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomAccountLogRepositoryImpl implements CustomAccountLogRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QAccountLog qAccountLog = new QAccountLog("accountLog");

    @Override
    public Optional<AccountLog> findByAccountIdAndDeviceIdAndLoginAt(Long accountId, String deviceId, LocalDate loginAt) {
        AccountLog accountLog = jpaQueryFactory
                .selectFrom(qAccountLog)
                .where(qAccountLog.accountId.eq(accountId), qAccountLog.deviceId.eq(deviceId), qAccountLog.loginAt.eq(loginAt))
                .fetchOne();

        return Optional.ofNullable(accountLog);
    }
}
