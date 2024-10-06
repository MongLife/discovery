package com.monglife.discovery.auth.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleCode {
    NORMAL("NORMAL", "일반회원"),
    ADMIN("ADMIN", "관리자"),
    ;

    private final String role;
    private final String name;
}
