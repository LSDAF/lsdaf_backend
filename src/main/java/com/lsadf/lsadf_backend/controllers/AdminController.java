package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.SortingOrderParameter;
import com.lsadf.lsadf_backend.requests.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(value = ControllerConstants.ADMIN)
@Tag(name = ControllerConstants.Swagger.ADMIN_CONTROLLER)
public interface AdminController {

    String BEGIN_DATE_QUERY_PARAM = "begin_date";
    String END_DATE_QUERY_PARAM = "end_date";
    String STATUS_QUERY_PARAM = "status";
    String TYPE_QUERY_PARAM = "type";
    String SORTING_ORDER_QUERY_PARAM = "sort";
    String USER_ID_QUERY_PARAM = "user_id";

    /**
     * Gets the global info of the application
     *
     * @return the global info
     */
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets all users")
    ResponseEntity<GenericResponse<List<User>>> getUsers(@RequestParam(value = SORTING_ORDER_QUERY_PARAM, required = false) SortingOrderParameter updateTimeSorting);

    /**
     * Gets a user by its id
     *
     * @param userId the id of the user
     * @return the user
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a user by its id")
    ResponseEntity<GenericResponse<User>> getUser(String userId);

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
    @Operation(summary = "Updates a user")
    ResponseEntity<GenericResponse<User>> updateUser(String userId, User user);

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
    ResponseEntity<GenericResponse<User>> createUser(UserCreationRequest userCreationRequest);

    // GameSave

    /**
     * Gets all game saves with given criterias
     *
     * @param updateTimeSorting the sorting order
     * @return the game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Gets a save game by its id")
    ResponseEntity<GenericResponse<GameSave>> getSaveGames(@RequestParam(value = SORTING_ORDER_QUERY_PARAM, required = false) SortingOrderParameter updateTimeSorting,
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
    ResponseEntity<GenericResponse<GameSave>> updateGameSave(String gameSaveId, GameSave gameSave);

    /**
     * Deletes a game save
     *
     * @return the deleted game save
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Generates a new game save")
    ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame();

    /**
     * Searches for users in function of the given search criteria
     *
     * @param searchRequest the search criteria
     * @return the list of users
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Searches for users in function of the give search criteria")
    ResponseEntity<GenericResponse<GameSave>> searchUsers(@RequestBody SearchRequest searchRequest);

    /**
     * Searches for game saves in function of the given search criteria
     *
     * @param searchRequest the search criteria
     * @return the list of game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Searches for game saves in function of the give search criteria")
    ResponseEntity<GenericResponse<GameSave>> searchGameSaves(@RequestBody SearchRequest searchRequest);
}
