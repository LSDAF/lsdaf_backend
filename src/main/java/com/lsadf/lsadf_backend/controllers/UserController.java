package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

/**
 * Controller for user operations
 */
@RequestMapping(value = ControllerConstants.USER)
@Tag(name = ControllerConstants.Swagger.USER_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
public interface UserController {

    /**
     * Gets the user info
     * @param localUser the authenticated user to get info from
     * @return the user information
     */
    @GetMapping(value = ControllerConstants.User.INFO)
    ResponseEntity<GenericResponse<UserInfo>> getUserInfo(@CurrentUser LocalUser localUser) throws UnauthorizedException;
}
