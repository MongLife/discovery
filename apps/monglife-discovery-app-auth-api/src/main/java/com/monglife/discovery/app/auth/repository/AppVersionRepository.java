package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.AppVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppVersionRepository extends JpaRepository<AppVersionEntity, Long> {

    Optional<AppVersionEntity> findByAppPackageNameAndBuildVersion(String appPackageName, String buildVersion);
}
