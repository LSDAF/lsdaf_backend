package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.LocalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import static com.lsadf.lsadf_backend.models.LocalUser.buildSimpleGrantedAuthorities;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional
    public LocalUser loadUserById(String id) {
        UserEntity userEntity = userService.getUserById(id);
        return createLocalUser(userEntity);
    }

    @Override
    @Transactional
    public LocalUser loadUserByEmail(String email) {
        UserEntity userEntity = userService.getUserByEmail(email);
        return createLocalUser(userEntity);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        UserEntity userEntity = userService.getUserByEmail(username);
        return createLocalUser(userEntity);
    }

    private LocalUser createLocalUser(UserEntity user) {
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
