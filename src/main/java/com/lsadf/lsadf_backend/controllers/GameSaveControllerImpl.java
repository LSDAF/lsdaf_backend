package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of the GameSaveController.
 */
@RestController
@Slf4j
public class GameSaveControllerImpl extends BaseController implements GameSaveController {

    private final GameSaveService gameSaveService;
    private final Mapper mapper;

    public GameSaveControllerImpl(GameSaveService gameSaveService,
                                  Mapper mapper) {
        this.gameSaveService = gameSaveService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);

            String email = localUser.getUsername();

            GameSaveEntity newSave = gameSaveService.createGameSave(email);

            log.info("Successfully created new game for user with email {}", email);
            GameSave newGameSave = mapper.mapToGameSave(newSave);

            return ResponseUtils.generateResponse(HttpStatus.OK, "Successfully created new game for user with email " + email, newGameSave);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while generating new save: ", e);
            return ResponseUtils.generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while generating new save.", null);
        } catch (Exception e) {
            log.error("Exception {} while generating new save: ", e.getClass(), e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while generating new save.", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveGame(@CurrentUser LocalUser localUser,
                                                          @PathVariable(value = GAME_SAVE_ID) String id,
                                                          GameSaveUpdateRequest updateRequest) {
        try {
            validateUser(localUser);

            gameSaveService.updateGameSave(id, updateRequest);
            log.info("Successfully saved game with id {} for user with email {}", id, localUser.getUsername());
            return null;
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while saving game: ", e);
            return ResponseUtils.generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while saving game.", null);
        } catch (Exception e) {
            log.error("Exception {} while saving game: ", e.getClass(), e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while saving game.", e.getMessage());
        }
    }
}
