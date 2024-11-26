package com.monglife.discovery.app.auth.repositoryCustom;

import com.monglife.discovery.app.auth.domain.AccountEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.monglife.discovery.app.auth.domain.QAccountEntity.accountEntity;

@Repository
@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<AccountEntity> findByEmail(String email) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(accountEntity)
                .where(accountEntity.email.eq(email), accountEntity.isDeleted.eq(false))
                .fetchOne());
    }

    @Override
    public Optional<AccountEntity> findByAccountId(Long accountId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(accountEntity)
                .where(accountEntity.accountId.eq(accountId), accountEntity.isDeleted.eq(false))
                .fetchOne());
    }
}
