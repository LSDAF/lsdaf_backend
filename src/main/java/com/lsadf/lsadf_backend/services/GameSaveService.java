package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.models.GameSave;

import java.util.stream.Stream;

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
     * @param userEmail
     * @return
     * @throws ForbiddenException
     * @throws NotFoundException
     */
    GameSave getGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException, UnauthorizedException;

    /**
     * Updates a game save
     * @param saveId
     * @param newSaveGame
     * @param userEmail
     * @return
     * @throws ForbiddenException
     * @throws NotFoundException
     */
    GameSave updateGameSave(String saveId, GameSave newSaveGame, String userEmail) throws ForbiddenException, NotFoundException, UnauthorizedException;

    /**
     * Deletes a game save
     * @param saveId
     * @param userEmail
     * @throws ForbiddenException
     * @throws NotFoundException
     */
    void deleteGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException;

    /**
     * Gets all game saves
     * @return the stream of game saves
     */
    Stream<GameSaveEntity> getGameSaves();
}
