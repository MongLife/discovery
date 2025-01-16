package com.monglife.discovery.app.auth.account.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.discovery.app.auth.global.enums.AuthResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class AppVersionNotFoundException extends ErrorException {

    public AppVersionNotFoundException(String packageName, String buildVersion) {
        this.response = AuthResponse.DISCOVERY_AUTH_NOT_EXISTS_APP_VERSION;
        this.result = Map.of("packageName", packageName, "buildVersion", buildVersion);
    }
}
