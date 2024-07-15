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
     * Loads a user by email
     */
    LocalUser loadUserByEmail(String email);
}
