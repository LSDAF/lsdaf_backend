package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.*;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;

import java.util.List;

/**
 * Service for managing the admin panel
 */
public interface AdminService {
    /**
     * Gets the global info
     *
     * @return the global info
     */
    GlobalInfo getGlobalInfo();

    // Cache

    /**
     * Clears the caches of the application
     */
    void flushAndClearCache();

    /**
     * Toggles the cache
     */
    void toggleCache();

    /**
     * Checks if the cache is enabled
     *
     * @return true if the cache is enabled, false otherwise
     */
    boolean isRedisCacheEnabled();

    // Users

    /**
     * Gets all users
     *
     * @return the list of users
     */
    List<User> getUsers(UserOrderBy orderBy);

    /**
     * Gets a user by its id
     *
     * @param userId the id of the user
     * @return the user details
     * @throws NotFoundException if the user is not found
     */
    UserAdminDetails getUserById(String userId) throws NotFoundException;

    /**
     * Gets a user by its email
     *
     * @param userEmail the email of the user
     * @return the user details
     * @throws NotFoundException if the user is not found
     */
    UserAdminDetails getUserByEmail(String userEmail) throws NotFoundException;

    /**
     * Creates a new user
     *
     * @param creationRequest the user to create
     * @return the created user
     */
    User createUser(AdminUserCreationRequest creationRequest) throws AlreadyExistingUserException;

    /**
     * Updates a user
     *
     * @param userId            the id of the user
     * @param userUpdateRequest the request to update the user data
     * @return the updated user
     */
    User updateUser(String userId, AdminUserUpdateRequest userUpdateRequest) throws NotFoundException, AlreadyExistingUserException;

    /**
     * Deletes a user
     *
     * @param userId the id of the user
     * @throws NotFoundException if the user is not found
     */
    void deleteUser(String userId) throws NotFoundException;

    /**
     * Deletes a user by its email
     *
     * @param email the email of the user
     * @throws NotFoundException if the user is not found
     */
    void deleteUserByEmail(String email) throws NotFoundException;

    // GameSave

    /**
     * Gets all game saves
     *
     * @param orderBy the order by parameter
     * @return the game save
     */
    List<GameSave> getGameSaves(GameSaveOrderBy orderBy);

    /**
     * Gets a game save by its id
     *
     * @param saveId the id of the game save
     * @return the game save
     */
    GameSave getGameSave(String saveId) throws NotFoundException;

    /**
     * Updates a game save
     *
     * @param saveId        the id of the game save
     * @param updateRequest the update request
     * @return the updated game save
     */
    GameSave updateGameSave(String saveId, AdminGameSaveUpdateRequest updateRequest) throws ForbiddenException, UnauthorizedException, NotFoundException, AlreadyTakenNicknameException;

    /**
     * Deletes a game save
     *
     * @param saveId the id of the game save
     */
    void deleteGameSave(String saveId) throws ForbiddenException, NotFoundException;

    /**
     * Creates a new game save
     *
     * @param adminGameSaveCreationRequest the game save request
     * @return the created game save
     */
    GameSave createGameSave(AdminGameSaveCreationRequest adminGameSaveCreationRequest) throws NotFoundException, AlreadyExistingGameSaveException;

    /**
     * Searches users
     *
     * @param searchRequest the filters to apply
     * @return the list of users
     */
    List<User> searchUsers(SearchRequest searchRequest, UserOrderBy orderBy);

    /**
     * Searches game saves
     *
     * @param searchRequest the filters to apply
     * @return the list of game saves
     */
    List<GameSave> searchGameSaves(SearchRequest searchRequest, GameSaveOrderBy orderBy);
}
