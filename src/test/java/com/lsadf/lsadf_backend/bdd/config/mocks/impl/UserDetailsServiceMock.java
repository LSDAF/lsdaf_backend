package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.lsadf.lsadf_backend.models.LocalUser.buildSimpleGrantedAuthorities;

public class UserDetailsServiceMock implements UserDetailsService {
    private final UserService userService;
    private final Mapper mapper;

    public UserDetailsServiceMock(UserService userService,
                                  Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }


    public LocalUser loadUserByEmail(String email) throws NotFoundException {
        UserEntity userEntity = userService.getUserByEmail(email);
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
                user.isEnabled(),
                true,
                true,
                true,
                buildSimpleGrantedAuthorities(user.getRoles()),
                user);
    }
}
