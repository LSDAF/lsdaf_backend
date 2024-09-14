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
    String createToken(LocalUser localUser);

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
}
