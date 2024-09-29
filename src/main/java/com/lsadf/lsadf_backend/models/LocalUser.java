package com.lsadf.lsadf_backend.models;

import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;


public class LocalUser extends User implements OAuth2User, OidcUser {

    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;
    @Setter
    private Map<String, Object> attributes;
    @Getter
    private final UserEntity userEntity;

    public LocalUser(final String userEmail, final String password, final boolean enabled, final boolean accountNonExpired, final boolean credentialsNonExpired,
                     final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities, final UserEntity userEntity) {
        this(userEmail, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, userEntity, null, null);
    }

    public LocalUser(final String userEmail, final String password, final boolean enabled, final boolean accountNonExpired, final boolean credentialsNonExpired,
                     final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities, final UserEntity userEntity, OidcIdToken idToken,
                     OidcUserInfo userInfo) {
        super(userEmail, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userEntity = userEntity;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    public static LocalUser create(UserEntity user, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
            LocalUser localUser = new LocalUser(user.getEmail(), user.getPassword(), user.getEnabled(), true, true, true, buildSimpleGrantedAuthorities(user.getRoles()),
                user, idToken, userInfo);
        localUser.setAttributes(attributes);
        return localUser;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return (A) this.attributes.get(name);
    }

    @Override
    public String getName() {
        return this.userEntity.getName();
    }

    public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<UserRole> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }
}
