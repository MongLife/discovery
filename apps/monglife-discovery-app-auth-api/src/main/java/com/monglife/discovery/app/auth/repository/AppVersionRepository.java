package com.monglife.discovery.app.auth.repository;

import com.monglife.discovery.app.auth.domain.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    Optional<AppVersion> findByAppCodeAndBuildVersion(String appCode, String buildVersion);
}
