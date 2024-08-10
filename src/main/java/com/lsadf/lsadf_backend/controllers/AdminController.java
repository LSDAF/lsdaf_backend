package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
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

@RequestMapping(value = ControllerConstants.ADMIN)
@Tag(name = ControllerConstants.Swagger.ADMIN_CONTROLLER)
public interface AdminController {

    String ORDER_BY_QUERY_PARAM = "order_by";
    String USER_ID_QUERY_PARAM = "user_id";

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
    ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo();

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
    ResponseEntity<GenericResponse<List<User>>> getUsers(@RequestParam(value = ORDER_BY_QUERY_PARAM, required = false) UserOrderBy orderBy);

    /**
     * Gets a user by its id
     *
     * @param userId the id of the user
     * @return the user details
     */
    @GetMapping(value = ControllerConstants.Admin.USER_ID)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a UserAdminDetails by the user id")
    ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserById(String userId);

    /**
     * Gets a user by its email
     *
     * @param userEmail the email of the user
     * @return the user
     */
    @GetMapping(value = ControllerConstants.Admin.USER_EMAIL)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a user by its email")
    ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserByEmail(String userEmail);

    /**
     * Updates a user
     *
     * @param userId the id of the user
     * @param user   the user to update
     * @return the updated user
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = ControllerConstants.Admin.USER_ID)
    @Operation(summary = "Updates a user")
    ResponseEntity<GenericResponse<User>> updateUser(String userId, @RequestBody User user);

    /**
     * Deletes a user
     *
     * @param userId the id of the user
     * @return the deleted user
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Deletes a user")
    @DeleteMapping(value = ControllerConstants.Admin.USER_ID)
    ResponseEntity<GenericResponse<User>> deleteUser(String userId);

    /**
     * Creates a new user
     *
     * @param userCreationRequest the user creation request
     * @return the created user
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Creates a new user")
    @PostMapping(value = ControllerConstants.Admin.CREATE_USER)
    ResponseEntity<GenericResponse<User>> createUser(@RequestBody UserCreationRequest userCreationRequest);

    // GameSave

    /**
     * Gets all game saves with given criterias
     *
     * @param orderBy the sorting order if any
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
    ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(@RequestParam(value = ORDER_BY_QUERY_PARAM, required = false) GameSaveOrderBy orderBy,
                                                           @RequestParam(value = USER_ID_QUERY_PARAM, required = false) String userId);

    /**
     * Gets a save game by its id
     *
     * @param gameSaveId the id of the game save
     * @return the game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a save game by its id")
    @GetMapping(value = ControllerConstants.Admin.GAME_SAVE_ID)
    ResponseEntity<GenericResponse<GameSave>> getGameSave(String gameSaveId);

    /**
     * Updates a game save
     *
     * @param gameSaveId the id of the game save
     * @param gameSave   the game save to update
     * @return the updated game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Updates a new game")
    @PostMapping(value = ControllerConstants.Admin.GAME_SAVE_ID)
    ResponseEntity<GenericResponse<GameSave>> updateGameSave(String gameSaveId, GameSave gameSave);

    /**
     * Creates a game save
     * @param creationRequest the creation request
     * @return the deleted game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Generates a new game save")
    @PostMapping(value = ControllerConstants.Admin.CREATE_GAME_SAVE)
    ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@RequestBody AdminGameSaveCreationRequest creationRequest);

    /**
     * Searches for users in function of the given search criteria
     *
     * @param searchRequest the search criteria
     * @param orderBy
     * @return the list of users
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = ControllerConstants.Admin.SEARCH_USERS)
    @Operation(summary = "Searches for users in function of the give search criteria")
    ResponseEntity<GenericResponse<User>> searchUsers(@RequestBody SearchRequest searchRequest, UserOrderBy orderBy);

    /**
     * Searches for game saves in function of the given search criteria
     *
     * @param searchRequest the search criteria
     * @param orderBy
     * @return the list of game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = ControllerConstants.Admin.SEARCH_GAME_SAVES)
    @Operation(summary = "Searches for game saves in function of the give search criteria")
    ResponseEntity<GenericResponse<GameSave>> searchGameSaves(@RequestBody SearchRequest searchRequest, GameSaveOrderBy orderBy);
}
