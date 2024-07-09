package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.GameSave;

/**
 * Service for managing game saves
 */
public interface GameSaveService {
    GameSave createGameSave(String userEmail) throws NotFoundException;
    GameSave getGameSave(String saveId) throws ForbiddenException, NotFoundException;
    GameSave getGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException;
    GameSave updateGameSave(String saveId, GameSave newSaveGame, String userEmail) throws ForbiddenException, NotFoundException;
    void deleteGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException;
}
