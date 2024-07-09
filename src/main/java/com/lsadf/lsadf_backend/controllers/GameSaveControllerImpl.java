package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class GameSaveControllerImpl implements GameSaveController {

    private final GameSaveService gameSaveService;

    public GameSaveControllerImpl(GameSaveService gameSaveService) {
        this.gameSaveService = gameSaveService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame() {
        try {
            String userId = UUID.randomUUID().toString();
            GameSave newSave = gameSaveService.createGameSave(userId);

            return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully created new game for userId " + userId, newSave);
        } catch (Exception e) {
            log.error("Exception {} while generating new save: ", e.getClass(), e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while generating new save.", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> getGameSave(@org.hibernate.validator.constraints.UUID @PathVariable(value = GAME_SAVE_ID) String id) {
        try {
            String userId = UUID.randomUUID().toString();

            GameSave gameSave = gameSaveService.getGameSave(id);

            return ResponseUtils.generateResponse(HttpStatus.OK, "", gameSave);

        } catch (Exception e) {
            log.error("Exception {} while saving game: ", e.getClass(), e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting game save.", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveGame(@org.hibernate.validator.constraints.UUID @PathVariable(value = GAME_SAVE_ID) String id,
                                                          GameSave save) {
        try {
            return null;
        } catch (Exception e) {
            log.error("Exception {} while saving game: ", e.getClass(), e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while saving game.", e.getMessage());
        }
    }
}
