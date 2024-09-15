package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin controller
 */
@RequestMapping(value = ControllerConstants.ADMIN)
@Tag(name = ControllerConstants.Swagger.ADMIN_CONTROLLER)
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
    ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo(LocalUser localUser);


    // Cache

    /**
     * Clears the cache
     * @param localUser the user
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
    ResponseEntity<GenericResponse<Void>> flushAndClearCache(LocalUser localUser);

    /**
     * Checks if the cache is enabled
     * @param localUser the user
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
    ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(LocalUser localUser);

    /**
     * Enables/Disables the cache
     *
     * @param localUser the user
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
    ResponseEntity<GenericResponse<Boolean>> toggleCacheEnabling(LocalUser localUser);

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
    ResponseEntity<GenericResponse<List<User>>> getUsers(LocalUser localUser, UserOrderBy orderBy);

    /**
     * Gets a user by its id
     *
     * @param localUser
     * @param userId    the id of the user
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
    ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserById(LocalUser localUser, String userId);

    /**
     * Gets a user by its email
     *
     * @param localUser
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
    ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserByEmail(LocalUser localUser, String userEmail);

    /**
     * Updates a user
     *
     * @param localUser
     * @param userId    the id of the user
     * @param user      the user to update
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
    ResponseEntity<GenericResponse<User>> updateUser(LocalUser localUser, String userId, AdminUserUpdateRequest user);

    /**
     * Deletes a user
     *
     * @param localUser
     * @param userId    the id of the user
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
    ResponseEntity<GenericResponse<Void>> deleteUser(LocalUser localUser, String userId);

    /**
     * Creates a new user
     *
     * @param localUser
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
    ResponseEntity<GenericResponse<User>> createUser(LocalUser localUser, AdminUserCreationRequest adminUserCreationRequest);

    // GameSave

    /**
     * Gets all game saves with given criterias
     *
     * @param localUser
     * @param orderBy   the sorting order if any
     * @return the game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a save game by its id")
    @GetMapping(value = ControllerConstants.Admin.GAME_SAVES)
    ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(LocalUser localUser, GameSaveOrderBy orderBy);

    /**
     * Gets a save game by its id
     *
     * @param localUser
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
    @Operation(summary = "Gets a save game by its id")
    @GetMapping(value = ControllerConstants.Admin.GAME_SAVE_ID)
    ResponseEntity<GenericResponse<GameSave>> getGameSave(LocalUser localUser, String gameSaveId);

    /**
     * Updates a game save
     *
     * @param localUser
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
    ResponseEntity<GenericResponse<GameSave>> updateGameSave(LocalUser localUser, String gameSaveId, AdminGameSaveUpdateRequest adminGameSaveUpdateRequest);

    /**
     * Creates a game save
     *
     * @param localUser
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
    ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(LocalUser localUser, AdminGameSaveCreationRequest creationRequest);

    /**
     * Deletes a game save
     *
     * @param localUser
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
    ResponseEntity<GenericResponse<Void>> deleteGameSave(LocalUser localUser, String gameSaveId);

    /**
     * Searches for users in function of the given search criteria
     *
     * @param localUser
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
    ResponseEntity<GenericResponse<User>> searchUsers(LocalUser localUser, SearchRequest searchRequest, UserOrderBy orderBy);

    /**
     * Searches for game saves in function of the given search criteria
     *
     * @param localUser
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
    ResponseEntity<GenericResponse<GameSave>> searchGameSaves(LocalUser localUser, SearchRequest searchRequest, GameSaveOrderBy orderBy);
}
