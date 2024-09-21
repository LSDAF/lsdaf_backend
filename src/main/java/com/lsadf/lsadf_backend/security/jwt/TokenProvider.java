package com.lsadf.lsadf_backend.security.jwt;

import com.lsadf.lsadf_backend.models.LocalUser;

/**
 * Interface for the token provider.
 */
public interface TokenProvider {
    /**
     * Creates a token.
     *
     * @param localUser@return
     */
    String createJwtToken(LocalUser localUser);

    /**
     * Gets the id from the token.
     * @param token
     * @return
     */
    String getUserIdFromToken(String token);

    /**
     * Validates the token.
     * @param authToken
     * @return
     */
    boolean validateToken(String authToken);

    /**
     * Invalidates the token.
     * @param token the token to invalidate
     * @param userEmail the email of the user
     */
    void invalidateToken(String token, String userEmail);
}
