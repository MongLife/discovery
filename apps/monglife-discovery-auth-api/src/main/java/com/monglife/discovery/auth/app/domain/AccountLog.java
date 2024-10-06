package com.monglife.discovery.auth.app.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class AccountLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountLogId;

    @Column(updatable = false, nullable = false)
    private Long accountId;

    @Column(updatable = false, nullable = false)
    private String deviceId;

    @Column(updatable = false, nullable = false)
    @CreatedDate
    private LocalDate loginAt;

    @Column(nullable = false)
    @Builder.Default
    private Integer loginCount = 0;

    public void increaseLoginCount() {
        this.loginCount++;
    }
}
