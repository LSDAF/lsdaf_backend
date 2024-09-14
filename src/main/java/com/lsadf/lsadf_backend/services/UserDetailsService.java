package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.LocalUser;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    /**
     * Loads a user by email
     */
    LocalUser loadUserByEmail(String email) throws NotFoundException;
}
