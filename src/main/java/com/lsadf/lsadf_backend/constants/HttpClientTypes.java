package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpClientTypes {
    public static final String DEFAULT = "default";
    public static final String KEYCLOAK = "keycloak";
    public static final String KEYCLOAK_ADMIN = "keycloak-admin";
}
