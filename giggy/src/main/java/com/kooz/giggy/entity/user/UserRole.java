package com.kooz.giggy.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    EMPLOYER("ROLE_EMPLOYER", "EMPLOYER"),
    EMPLOYEE("ROLE_EMPLOYEE", "EMPLOYEE");

    private final String key;
    private final String value;
}