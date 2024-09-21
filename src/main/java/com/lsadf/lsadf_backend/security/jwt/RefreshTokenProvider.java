package com.lsadf.lsadf_backend.security.jwt;

import com.lsadf.lsadf_backend.entities.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.LocalUser;

public interface RefreshTokenProvider {

    /**
     * Creates a refresh token.
     * @param localUser the local user
     * @return the generated refresh token
     */
    String createRefreshToken(LocalUser localUser);

    /**
     * Saves a refresh token in database.
     * @param token the token to save
     * @return the saved refresh token
     */
    RefreshTokenEntity saveRefreshToken(UserEntity userEntity, String token);

    /**
     * Invalidates a refresh token by token.
     * @param userEmail the email of the user
     * @return the found refresh token
     */
    void invalidateUserRefreshToken(String userEmail) throws NotFoundException;

    /**
     * Deletes all expired tokens from database.
     */
    void deleteExpiredTokens();

    /**
     * Validates a refresh token.
     *
     * @param token     the token to validate
     * @param userEmail the email of the user
     * @throws NotFoundException     if the token is not found
     * @throws InvalidTokenException if the token is expired
     */
    void validateRefreshToken(String token, String userEmail) throws NotFoundException, InvalidTokenException;
}
