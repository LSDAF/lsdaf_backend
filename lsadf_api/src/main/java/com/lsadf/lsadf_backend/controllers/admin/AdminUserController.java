package com.lsadf.lsadf_backend.controllers.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.annotations.Uuid;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.constants.JsonViews;
import com.lsadf.core.constants.ResponseMessages;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.admin.AdminUserCreationRequest;
import com.lsadf.core.requests.admin.AdminUserUpdateRequest;
import com.lsadf.core.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lsadf.core.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.core.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;
import static com.lsadf.core.constants.ControllerConstants.Params.*;

@RequestMapping(value = ControllerConstants.ADMIN_USERS)
@Tag(name = ControllerConstants.Swagger.ADMIN_USERS_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminUserController {

    /**
     * Updates a user
     *
     * @param jwt    the requester JWT
     * @param userId the id of the user
     * @param user   the user to update
     * @return the updated user
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = ControllerConstants.AdminUser.USER_ID)
    @Operation(summary = "Updates a user")
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<User>> updateUser(@AuthenticationPrincipal Jwt jwt,
                                                     @PathVariable(value = USER_ID) @Uuid String userId,
                                                     @Valid @RequestBody AdminUserUpdateRequest user);

    /**
     * Creates a new user
     *
     * @param jwt                      the requester JWT
     * @param adminUserCreationRequest the user creation request
     * @return the created user
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "403", description = ResponseMessages.BAD_REQUEST),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Creates a new user")
    @JsonView(JsonViews.Admin.class)
    @PostMapping
    ResponseEntity<GenericResponse<User>> createUser(@AuthenticationPrincipal Jwt jwt,
                                                     @Valid @RequestBody AdminUserCreationRequest adminUserCreationRequest);

    /**
     * Deletes a user
     *
     * @param jwt    the requester JWT
     * @param userId the id of the user
     * @return empty response
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Deletes a user")
    @DeleteMapping(value = ControllerConstants.AdminUser.USER_ID)
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<Void>> deleteUser(@AuthenticationPrincipal Jwt jwt,
                                                     @PathVariable(value = USER_ID) @Uuid String userId);

    /**
     * Gets a user by its email
     *
     * @param jwt       the requester JWT
     * @param username the email of the user
     * @return the user
     */
    @GetMapping(value = ControllerConstants.AdminUser.USERNAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a user by its email")
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<User>> getUserByUsername(@AuthenticationPrincipal Jwt jwt,
                                                            @Valid @Email @PathVariable(value = USERNAME) String username);

    /**
     * Gets a user by its id
     *
     * @param jwt    the requester JWT
     * @param userId the id of the user
     * @return the user details
     */
    @GetMapping(value = ControllerConstants.AdminUser.USER_ID)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a UserAdminDetails by the user id")
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<User>> getUserById(@AuthenticationPrincipal Jwt jwt,
                                                      @PathVariable(value = USER_ID) @Uuid String userId);


    /**
     * Gets all users
     *
     * @return the list of users
     */
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets all users")
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<List<User>>> getUsers(@AuthenticationPrincipal Jwt jwt,
                                                         @RequestParam(value = ORDER_BY, required = false) String orderBy);
}
