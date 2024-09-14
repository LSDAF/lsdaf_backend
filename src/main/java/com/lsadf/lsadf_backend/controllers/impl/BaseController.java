package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.models.LocalUser;
import org.slf4j.Logger;

/**
 * Base controller for all controllers
 */
public abstract class BaseController {

    /**
     * Gets the controller logger
     * @return the logger
     */
    protected abstract Logger getLogger();

    /**
     * Validates the user
     *
     * @param localUser the user to validate
     * @throws UnauthorizedException if the user is not valid
     */
    protected void validateUser(LocalUser localUser) throws UnauthorizedException {
        if (localUser == null) {
            Logger logger = getLogger();
            logger.error("Unauthorized. Didn't manage to build UserInfo from token. Please login");
            throw new UnauthorizedException("Unauthorized. Didn't manage to build UserInfo from token. Please login");
        }
    }
}
