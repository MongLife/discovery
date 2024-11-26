package com.monglife.discovery.app.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class AppVersionEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appVersionId;

    @Column(nullable = false)
    private String appCode;

    @Column(nullable = false, updatable = false)
    private String buildVersion;

    @Column(nullable = false)
    private Boolean mustUpdate;


    @Builder
    public AppVersionEntity(String appCode, String buildVersion, Boolean mustUpdate) {
        this.appCode = appCode;
        this.buildVersion = buildVersion;
        this.mustUpdate = mustUpdate;
    }
}