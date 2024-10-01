package com.lsadf.lsadf_backend.security.jwt;

import com.lsadf.lsadf_backend.entities.AEntity;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.exceptions.InvalidTokenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.LocalUser;

import java.util.List;

/**
 * Interface for the token provider.
 */
public interface TokenProvider<T extends AEntity> {
    /**
     * Creates a token.
     *
     * @param localUser@return
     */
    T createToken(LocalUser localUser);

    /**
     * Gets the user email from the token.
     * @param token the token
     * @return the user email
     */
    String getUserEmailFromToken(String token);

    /**
     * Validates the token.
     * @param authToken
     * @return
     */
    boolean validateToken(String authToken, String userEmail) throws NotFoundException, InvalidTokenException;

    /**
     * Invalidates the token.
     * @param token the token to invalidate
     * @param userEmail the email of the user
     */
    void invalidateToken(String token, String userEmail) throws NotFoundException, InvalidTokenException;

    /**
     * Gets all invalidated tokens.
     * @return the list of invalidated tokens
     */
    List<JwtTokenEntity> getInvalidatedTokens();

    /**
     * Deletes all expired tokens.
     */
    void deleteExpiredTokens();

}
