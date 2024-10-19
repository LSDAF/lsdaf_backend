package com.lsadf.lsadf_backend.bdd.config.mocks.security;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.services.UserEntityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.lsadf.lsadf_backend.models.LocalUser.buildSimpleGrantedAuthorities;

/**
 * Mock implementation of the UserDetailsService
 */
public class LsadfUserDetailsServiceMock implements UserDetailsService {
    private final UserEntityService userEntityService;

    public LsadfUserDetailsServiceMock(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }


    public LocalUser loadUserByEmail(String email) throws NotFoundException {
        UserEntity userEntity = userEntityService.getUserByEmail(email);
        return createLocalUser(userEntity);
    }

    public UserDetails loadUserByUsername(final String username) {
        try {
            return this.loadUserByEmail(username);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    private static LocalUser createLocalUser(UserEntity user) {
        return new LocalUser(user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                buildSimpleGrantedAuthorities(null),
                user);
    }
}
