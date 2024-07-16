package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
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
    List<User> getUsers();

    /**
     * Gets a user by its id
     *
     * @param userId the id of the user
     * @return
     */
    User getUser(String userId);

    /**
     * Creates a new user
     *
     * @param user the user to create
     * @return
     */
    User createUser(User user);

    /**
     * Updates a user
     *
     * @param userId the id of the user
     * @param user   the user to update
     * @return
     */
    User updateUser(String userId, User user);

    /**
     * Deletes a user
     *
     * @param userId the id of the user
     */
    void deleteUser(String userId);

    // GameSave

    /**
     * Gets all game saves
     *
     * @param saveId the id of the game save
     * @return the game save
     */
    GameSave getGameSave(String saveId);

    /**
     * Updates a game save
     *
     * @param saveId      the id of the game save
     * @param newSaveGame the new game save
     * @return the updated game save
     */
    GameSave updateGameSave(String saveId, GameSave newSaveGame);

    /**
     * Deletes a game save
     *
     * @param saveId the id of the game save
     */
    void deleteGameSave(String saveId);

    /**
     * Creates a new game save
     *
     * @param userEmail the email of the user
     * @return the created game save
     */
    GameSave createGameSave(String userEmail);

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
