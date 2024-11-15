package com.lsadf.lsadf_backend.services;

import com.lsadf.core.entities.GameSaveEntity;
import com.lsadf.core.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.core.exceptions.AlreadyTakenNicknameException;
import com.lsadf.core.exceptions.http.ForbiddenException;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.core.exceptions.http.UnauthorizedException;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;

import java.util.stream.Stream;

/**
 * Service for managing game saves
 */
public interface GameSaveService {
    /**
     * Creates a new game save for the user
     *
     * @param userEmail the user email
     * @return the created game save
     * @throws NotFoundException the not found exception
     */
    GameSaveEntity createGameSave(String userEmail) throws NotFoundException;

    /**
     * Creates a new game save
     *
     * @param creationRequest the admin creation request
     * @return the created game save
     * @throws NotFoundException the not found exception
     */
    GameSaveEntity createGameSave(AdminGameSaveCreationRequest creationRequest) throws NotFoundException, AlreadyExistingGameSaveException;

    /**
     * Gets a game save
     *
     * @param saveId the save id
     * @return the game save
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    GameSaveEntity getGameSave(String saveId) throws NotFoundException;

    /**
     * Updates a game save
     *
     * @param saveId                        the save id
     * @param gameSaveUpdateNicknameRequest the nickname update request
     * @return the updated game save
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    GameSaveEntity updateNickname(String saveId, GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest) throws ForbiddenException, NotFoundException, UnauthorizedException, AlreadyTakenNicknameException;

    /**
     * Updates a game save from admin side
     *
     * @param saveId        the save id
     * @param updateRequest the admin update request
     * @return the updated game save
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    GameSaveEntity updateNickname(String saveId, AdminGameSaveUpdateRequest updateRequest) throws ForbiddenException, NotFoundException, UnauthorizedException, AlreadyTakenNicknameException;

    /**
     * Checks if a game save exists
     * @param gameSaveId the game save id
     * @return true if the game save exists, false otherwise
     */
    boolean existsById(String gameSaveId);

    /**
     * Deletes a game save
     *
     * @param saveId the save id
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    void deleteGameSave(String saveId) throws NotFoundException;

    /**
     * Gets all game saves
     *
     * @return the stream of game saves
     */
    Stream<GameSaveEntity> getGameSaves();


    /**
     * Gets all game saves of a user
     *
     * @param userEmail the user email
     * @return the stream of game saves
     */
    Stream<GameSaveEntity> getGameSavesByUsername(String userEmail);

    /**
     * Checks if the user owns the game save
     *
     * @param saveId    the game save id
     * @param userEmail the user email
     * @throws ForbiddenException the forbidden exception
     * @throws NotFoundException  the not found exception
     */
    void checkGameSaveOwnership(String saveId, String userEmail) throws ForbiddenException, NotFoundException;
}
