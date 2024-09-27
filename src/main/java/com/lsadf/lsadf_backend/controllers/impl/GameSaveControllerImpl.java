package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.controllers.GameSaveController;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyTakenNicknameException;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.GameSaveService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

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
        return log;
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

            return generateResponse(HttpStatus.OK, newGameSave);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while generating new save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while generating new save.", null);
        } catch (Exception e) {
            log.error("Exception {} while generating new save: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while generating new save.", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveGame(@CurrentUser LocalUser localUser,
                                                          @PathVariable(value = GAME_SAVE_ID) String id,
                                                          @Valid @RequestBody GameSaveUpdateRequest updateRequest) {
        try {
            validateUser(localUser);
            gameSaveService.checkGameSaveOwnership(id, localUser.getUsername());
            gameSaveService.updateGameSave(id, updateRequest);
            log.info("Successfully saved game with id {} for user with email {}", id, localUser.getUsername());
            return generateResponse(HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error("Not found exception while saving game: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, "Game save not found.", null);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while saving game: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while saving game.", null);
        } catch (ForbiddenException e) {
            log.error("Forbidden exception while saving game: ", e);
            return generateResponse(HttpStatus.FORBIDDEN, "The given userEmail is not the owner of the game save.", null);
        } catch (AlreadyTakenNicknameException e) {
            log.error("AlreadyTakenNicknameException exception while saving game: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, "The given nickname is already taken.", null);
        } catch (Exception e) {
            log.error("Exception {} while saving game: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while saving game.", e.getMessage());
        }
    }
}
