package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.GameSave;

/**
 * Service for managing game saves
 */
public interface GameSaveService {
    /**
     * Creates a new game save for the user
     * @param userEmail
     * @return
     * @throws NotFoundException
     */
    GameSave createGameSave(String userEmail) throws NotFoundException;

    /**
     * Gets a game save
     * @param saveId
     * @return
     * @throws ForbiddenException
     * @throws NotFoundException
     */
    GameSave getGameSave(String saveId) throws ForbiddenException, NotFoundException;

    /**
     * Gets a game save
     * @param saveId
     * @param userEmail
     * @return
     * @throws ForbiddenException
     * @throws NotFoundException
     */
    GameSave getGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException;

    /**
     * Updates a game save
     * @param saveId
     * @param newSaveGame
     * @param userEmail
     * @return
     * @throws ForbiddenException
     * @throws NotFoundException
     */
    GameSave updateGameSave(String saveId, GameSave newSaveGame, String userEmail) throws ForbiddenException, NotFoundException;

    /**
     * Deletes a game save
     * @param saveId
     * @param userEmail
     * @throws ForbiddenException
     * @throws NotFoundException
     */
    void deleteGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException;
}
