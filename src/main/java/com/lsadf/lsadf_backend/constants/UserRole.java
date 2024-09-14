package com.lsadf.lsadf_backend.constants;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String role) {
        this.role = role;
    }

    private final String role;

    public String getName() {
        return this.name();
    }

    public static UserRole fromRole(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getRole().equals(role)) {
                return userRole;
            }
        }
        return null;
    }

    public static UserRole fromName(String name) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equals(name)) {
                return userRole;
            }
        }
        return null;
    }

    public static UserRole getDefaultRole() {
        return USER;
    }
}
