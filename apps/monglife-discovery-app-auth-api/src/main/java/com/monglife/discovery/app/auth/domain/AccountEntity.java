package com.monglife.discovery.app.auth.domain;

import com.monglife.core.enums.role.RoleCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(updatable = false, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false)
    private String role = RoleCode.NORMAL.getRole();


    @Builder
    public AccountEntity(String email, String name, Boolean isDeleted, String role) {
        this.email = email;
        this.name = name;
        this.isDeleted = isDeleted;
        this.role = role;
    }
}
