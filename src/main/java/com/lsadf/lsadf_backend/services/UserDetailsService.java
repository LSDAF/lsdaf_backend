package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.User;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    /**
     * Loads a user by id
     * @param id
     * @return
     */
    LocalUser loadUserById(String id);

    /**
     *
     */
    LocalUser loadUserByEmail(String email);
}
