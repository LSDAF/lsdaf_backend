package com.lsadf.core.controllers;

import com.lsadf.core.exceptions.http.UnauthorizedException;
import org.slf4j.Logger;
import org.springframework.security.oauth2.jwt.Jwt;

public interface Controller {

    /**
     * Gets the controller logger
     * @return the logger
     */
    Logger getLogger();

    /**
     * Validates the user
     *
     * @param jwt the jwt
     * @throws UnauthorizedException if the user is not valid
     */
    void validateUser(Jwt jwt) throws UnauthorizedException;
}
