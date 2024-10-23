package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.annotations.Uuid;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.responses.GenericResponse;
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

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

/**
 * Admin controller
 */
@RequestMapping(value = ControllerConstants.ADMIN)
@Tag(name = ControllerConstants.Swagger.ADMIN_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminController {

    String ORDER_BY = "order_by";
    String USER_ID = "user_id";
    String GAME_SAVE_ID = "game_save_id";
    String USER_EMAIL = "user_email";

    /**
     * Gets the global info of the application
     *
     * @return the global info
     */
    @GetMapping(value = ControllerConstants.Admin.GLOBAL_INFO)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets the global info of the application")
    ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo(@AuthenticationPrincipal Jwt jwt);


    // Cache

    /**
     * Clears all the caches of the application
     *
     * @param jwt the requester JWT
     * @return empty response
     */
    @PutMapping(value = ControllerConstants.Admin.CACHE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Clears all the caches of the application")
    ResponseEntity<GenericResponse<Void>> flushAndClearCache(@AuthenticationPrincipal Jwt jwt);

    /**
     * Checks if the cache is enabled
     *
     * @param jwt the requester JWT
     * @return true if the cache is enabled, false otherwise
     */
    @GetMapping(value = ControllerConstants.Admin.CACHE_ENABLED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Checks if the cache is enabled")
    ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(@AuthenticationPrincipal Jwt jwt);

    /**
     * Enables/Disables the cache
     *
     * @param jwt the requester JWT
     * @return empty response
     */
    @PutMapping(value = ControllerConstants.Admin.TOGGLE_CACHE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Enables/Disables the cache")
    ResponseEntity<GenericResponse<Boolean>> toggleRedisCacheEnabling(@AuthenticationPrincipal Jwt jwt);

    // User

    /**
     * Gets all users
     *
     * @return the list of users
     */
    @GetMapping(value = ControllerConstants.Admin.USERS)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets all users")
    ResponseEntity<GenericResponse<List<User>>> getUsers(@AuthenticationPrincipal Jwt jwt,
                                                         @RequestParam(value = ORDER_BY) UserOrderBy orderBy);

    /**
     * Gets a user by its id
     *
     * @param jwt    the requester JWT
     * @param userId the id of the user
     * @return the user details
     */
    @GetMapping(value = ControllerConstants.Admin.USER_ID)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a UserAdminDetails by the user id")
    ResponseEntity<GenericResponse<User>> getDetailedUserById(@AuthenticationPrincipal Jwt jwt,
                                                              @PathVariable(value = USER_ID) String userId);

    /**
     * Gets a user by its email
     *
     * @param jwt       the requester JWT
     * @param userEmail the email of the user
     * @return the user
     */
    @GetMapping(value = ControllerConstants.Admin.USER_EMAIL)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a user by its email")
    ResponseEntity<GenericResponse<User>> getDetailedUserByEmail(@AuthenticationPrincipal Jwt jwt,
                                                                 @Valid @Email @PathVariable(value = USER_EMAIL) String userEmail);

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
    @PostMapping(value = ControllerConstants.Admin.USER_ID)
    @Operation(summary = "Updates a user")
    ResponseEntity<GenericResponse<User>> updateUser(@AuthenticationPrincipal Jwt jwt,
                                                     @PathVariable(value = USER_ID) @Uuid String userId,
                                                     @Valid @RequestBody AdminUserUpdateRequest user);

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
    @DeleteMapping(value = ControllerConstants.Admin.USER_ID)
    ResponseEntity<GenericResponse<Void>> deleteUser(@AuthenticationPrincipal Jwt jwt,
                                                     @PathVariable(value = USER_ID) @Uuid String userId);

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
    @PostMapping(value = ControllerConstants.Admin.CREATE_USER)
    ResponseEntity<GenericResponse<User>> createUser(@AuthenticationPrincipal Jwt jwt,
                                                     @Valid @RequestBody AdminUserCreationRequest adminUserCreationRequest);

    // GameSave

    /**
     * Gets all game saves with given criterias
     *
     * @param jwt     the requester JWT
     * @param orderBy the sorting order if any
     * @return the game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a game save by its id")
    @GetMapping(value = ControllerConstants.Admin.GAME_SAVES)
    ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(@AuthenticationPrincipal Jwt jwt,
                                                                 @RequestParam(value = ORDER_BY, required = false) GameSaveOrderBy orderBy);

    /**
     * Gets a game save by its id
     *
     * @param jwt        the requester JWT
     * @param gameSaveId the id of the game save
     * @return the game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a game save by its id")
    @GetMapping(value = ControllerConstants.Admin.GAME_SAVE_ID)
    ResponseEntity<GenericResponse<GameSave>> getGameSave(@AuthenticationPrincipal Jwt jwt,
                                                          @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

    /**
     * Updates a game save
     *
     * @param jwt                        the requester JWT
     * @param gameSaveId                 the id of the game save
     * @param adminGameSaveUpdateRequest the game save to update
     * @return the updated game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Updates a new game")
    @PostMapping(value = ControllerConstants.Admin.GAME_SAVE_ID)
    ResponseEntity<GenericResponse<GameSave>> updateGameSave(@AuthenticationPrincipal Jwt jwt,
                                                             @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                             @Valid @RequestBody AdminGameSaveUpdateRequest adminGameSaveUpdateRequest);

    /**
     * Updates the currency of a game save
     *
     * @param jwt             the requester JWT
     * @param gameSaveId      the game save id
     * @param currencyRequest the currency request
     * @return the updated game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Updates the currency of a game save")
    @PostMapping(value = ControllerConstants.Admin.UPDATE_GAME_SAVE_CURRENCIES)
    ResponseEntity<GenericResponse<Void>> updateGameSaveCurrency(@AuthenticationPrincipal Jwt jwt,
                                                                 @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                                 @Valid @RequestBody CurrencyRequest currencyRequest);

    /**
     * Updates the stages of a game save
     *
     * @param jwt          the requester JWT
     * @param gameSaveId   the game save id
     * @param stageRequest the stage request
     * @return the updated game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Updates the stages of a game save")
    @PostMapping(value = ControllerConstants.Admin.UPDATE_GAME_SAVE_STAGES)
    ResponseEntity<GenericResponse<Void>> updateGameSaveStages(@AuthenticationPrincipal Jwt jwt,
                                                               @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                               @Valid @RequestBody StageRequest stageRequest);

    /**
     * Creates a game save
     *
     * @param jwt             the requester JWT
     * @param creationRequest the creation request
     * @return the created game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Generates a new game save")
    @PostMapping(value = ControllerConstants.Admin.CREATE_GAME_SAVE)
    ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@AuthenticationPrincipal Jwt jwt,
                                                                  @Valid @RequestBody AdminGameSaveCreationRequest creationRequest);

    /**
     * Deletes a game save
     *
     * @param jwt        the requester JWT
     * @param gameSaveId the id of the game save
     * @return empty response
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Deletes a game save")
    @DeleteMapping(value = ControllerConstants.Admin.GAME_SAVE_ID)
    ResponseEntity<GenericResponse<Void>> deleteGameSave(@AuthenticationPrincipal Jwt jwt,
                                                         @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);

    /**
     * Searches for users in function of the given search criteria
     *
     * @param jwt           the requester JWT
     * @param searchRequest the search criteria
     * @param orderBy       the sorting order if any
     * @return the list of users
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "400", description = ResponseMessages.BAD_REQUEST),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = ControllerConstants.Admin.SEARCH_USERS)
    @Operation(summary = "Searches for users in function of the give search criteria")
    ResponseEntity<GenericResponse<User>> searchUsers(@AuthenticationPrincipal Jwt jwt,
                                                      @Valid @RequestBody SearchRequest searchRequest,
                                                      @RequestParam(value = ORDER_BY) UserOrderBy orderBy);

    /**
     * Searches for game saves in function of the given search criteria
     *
     * @param jwt           the requester JWT
     * @param searchRequest the search criteria
     * @param orderBy
     * @return the list of game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "400", description = ResponseMessages.BAD_REQUEST),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = ControllerConstants.Admin.SEARCH_GAME_SAVES)
    @Operation(summary = "Searches for game saves in function of the give search criteria")
    ResponseEntity<GenericResponse<GameSave>> searchGameSaves(@AuthenticationPrincipal Jwt jwt,
                                                              @Valid @RequestBody SearchRequest searchRequest,
                                                              @RequestParam(value = ORDER_BY) GameSaveOrderBy orderBy);
}
