package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserRefreshLoginRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping(value = ControllerConstants.AUTH)
@Tag(name = ControllerConstants.Swagger.AUTH_CONTROLLER)
public interface AuthController {
    String TOKEN = "token";

    /**
     * Logs in a user
     *
     * @param userLoginRequest the user credentials to login
     * @return the JWT object containing the token
     */
    @PostMapping(value = ControllerConstants.Auth.LOGIN)
    @Operation(summary = "Logins a user, returns a JWT object contaning the token to request the API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<JwtAuthentication>> login(UserLoginRequest userLoginRequest) throws NotFoundException;

    /**
     * Login of a user with a refresh token
     *
     * @param userRefreshLoginRequest the refresh user login request
     * @return the new JWT object containing the token
     */
    @PostMapping(value = ControllerConstants.Auth.REFRESH_LOGIN)
    @Operation(summary = "Logs in a user with a refresh token instead of its password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<JwtAuthentication>> loginFromRefreshToken(UserRefreshLoginRequest userRefreshLoginRequest) throws NotFoundException;

    /**
     * Logs out a user
     *
     * @param localUser  the user to logout
     * @param authHeader the authorization header
     * @return a generic response
     * @throws NotFoundException if the user is not found
     */
    @PostMapping(value = ControllerConstants.Auth.LOGOUT)
    @Operation(summary = "Logs out a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<Void>> logout(LocalUser localUser,
                                                 String authHeader) throws NotFoundException;

    /**
     * Registers a new user
     *
     * @param userLoginRequest the credentials to register
     */
    @PostMapping(value = ControllerConstants.Auth.REGISTER)
    @Operation(summary = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "400", description = ResponseMessages.BAD_REQUEST),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<UserInfo>> register(UserCreationRequest userLoginRequest);

    /**
     * Validates a user account thanks to the token sent by email
     * @param token the token to validate the account
     * @return the user information
     */
    @GetMapping(value = ControllerConstants.Auth.VALIDATE_TOKEN)
    @Operation(summary = "Validates a user account thanks to the token sent by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "400", description = ResponseMessages.BAD_REQUEST),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<UserInfo>> validateUserAccount(String token);
}
