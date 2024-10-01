package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.entities.tokens.UserVerificationTokenEntity;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;

/**
 * Service for user validation
 */
public interface UserVerificationService {
    /**
     * Creates a user validation token
     *
     * @param userEntity the user entity
     * @return the created token
     */
    UserVerificationTokenEntity createUserValidationToken(UserEntity userEntity) throws NotFoundException;

    /**
     * Validates a user with the given email token
     * @param token the token provided in the validation email
     * @return the validated user
     */
    UserEntity validateUserVerificationToken(String token) throws NotFoundException, InvalidTokenException;
}
