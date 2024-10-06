package com.monglife.discovery.auth.app.repository;

import com.monglife.discovery.auth.app.domain.Account;
import com.monglife.discovery.auth.app.domain.QAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomAccountRepositoryImpl implements CustomAccountRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QAccount qAccount = new QAccount("account");

    @Override
    public Optional<Account> findByEmail(String email) {
        Account account = jpaQueryFactory
                .selectFrom(qAccount)
                .where(qAccount.email.eq(email), qAccount.isDeleted.eq(false))
                .fetchOne();

        return Optional.ofNullable(account);
    }

    @Override
    public Optional<Account> findByAccountId(Long accountId) {
        Account account = jpaQueryFactory
                .selectFrom(qAccount)
                .where(qAccount.accountId.eq(accountId), qAccount.isDeleted.eq(false))
                .fetchOne();

        return Optional.ofNullable(account);
    }
}
