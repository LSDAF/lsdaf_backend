package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.UserOrderBy;
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
     * @return
     */
    User getUser(String userId);

    User getUserById(String userEmail);

    /**
     * Creates a new user
     *
     * @param creationRequest the user to create
     * @return
     */
    User createUser(UserCreationRequest creationRequest);

    /**
     * Updates a user
     *
     * @param userId the id of the user
     * @param user   the user to update
     * @return
     */
    User updateUser(String userId, User user, String userEmail);

    /**
     * Deletes a user
     *
     * @param userId the id of the user
     */
    void deleteUser(String userId, String userEmail);

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
    GameSave getGameSave(String saveId, String userEmail) throws ForbiddenException, UnauthorizedException, NotFoundException;

    /**
     * Updates a game save
     *
     * @param saveId      the id of the game save
     * @param newSaveGame the new game save
     * @return the updated game save
     */
    GameSave updateGameSave(String saveId, GameSave newSaveGame, String userEmail) throws ForbiddenException, UnauthorizedException, NotFoundException;

    /**
     * Deletes a game save
     *
     * @param saveId the id of the game save
     */
    void deleteGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException;

    /**
     * Creates a new game save
     *
     * @param adminGameSaveCreationRequest the game save request
     * @return the created game save
     */
    GameSave createGameSave(AdminGameSaveCreationRequest adminGameSaveCreationRequest);

    /**
     * Searches users
     *
     * @param searchRequest the filters to apply
     * @return the list of users
     */
    List<User> searchUsers(SearchRequest searchRequest);

    /**
     * Searches game saves
     *
     * @param searchRequest the filters to apply
     * @return the list of game saves
     */
    List<GameSave> searchGameSaves(SearchRequest searchRequest);
}
