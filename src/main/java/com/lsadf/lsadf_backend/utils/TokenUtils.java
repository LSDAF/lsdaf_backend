package com.lsadf.lsadf_backend.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class TokenUtils {
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";

    /**
     * Get roles from claims
     * @param claims the claims
     * @return the roles
     */
    public static List<GrantedAuthority> getRolesFromClaims(Map<String, Object> claims) {
        List<String> roles = new ArrayList<>();
        if (claims.containsKey(REALM_ACCESS)) {
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get(REALM_ACCESS);
            roles = ((List<String>) realmAccess.get(ROLES));
        }
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }
}
