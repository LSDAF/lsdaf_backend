package com.lsadf.core.controllers.impl;

import com.lsadf.core.controllers.Controller;
import com.lsadf.core.exceptions.http.UnauthorizedException;
import org.slf4j.Logger;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Base controller for all controllers
 */
public abstract class BaseController implements Controller {

    /**
     * Validates the user
     *
     * @param jwt the jwt
     * @throws UnauthorizedException if the user is not valid
     */
    public void validateUser(Jwt jwt) throws UnauthorizedException {
        if (jwt == null) {
            Logger logger = getLogger();
            logger.error("Unauthorized. Didn't manage to build UserInfo from token. Please login");
            throw new UnauthorizedException("Didn't manage to build UserInfo from token. Please login");
        }
    }
}
