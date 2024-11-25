package com.monglife.discovery.app.auth.global.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.discovery.app.auth.global.response.AuthResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class AppVersionNotFoundException extends ErrorException {

    public AppVersionNotFoundException(String appCode, String buildVersion) {
        this.response = AuthResponse.AUTH_NOT_EXISTS_APP_VERSION;
        this.result = Map.of("appCode", appCode, "buildVersion", buildVersion);
    }
}
