package com.monglife.discovery.app.auth.repositoryCustom;

import com.monglife.discovery.app.auth.domain.LoginHistoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

import static com.monglife.discovery.app.auth.domain.QLoginHistoryEntity.loginHistoryEntity;

@Repository
@RequiredArgsConstructor
public class LoginHistoryCustomRepositoryImpl implements LoginHistoryCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LoginHistoryEntity> findByAccountIdAndDeviceIdAndLoginAt(Long accountId, String deviceId, LocalDate loginAt) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(loginHistoryEntity)
                .where(loginHistoryEntity.accountId.eq(accountId), loginHistoryEntity.deviceId.eq(deviceId), loginHistoryEntity.loginAt.eq(loginAt))
                .fetchOne());
    }
}
