package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.GameSaveController;
import com.lsadf.core.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.core.models.GameSave;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.GameSaveService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;
import static com.lsadf.lsadf_backend.utils.TokenUtils.getUsernameFromJwt;

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
    public ResponseEntity<GenericResponse<GameSave>> generateNewGameSave(Jwt jwt) {
        validateUser(jwt);

        String username = getUsernameFromJwt(jwt);

        GameSaveEntity newSave = gameSaveService.createGameSave(username);

        log.info("Successfully created new game for user with username {}", username);
        GameSave newGameSave = mapper.mapGameSaveEntityToGameSave(newSave);

        return generateResponse(HttpStatus.OK, newGameSave);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> updateNickname(Jwt jwt,
                                                                String id,
                                                                GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest) {
        validateUser(jwt);
        String username = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(id, username);
        gameSaveService.updateNickname(id, gameSaveUpdateNicknameRequest);
        log.info("Successfully saved game with id {} for user with email {}", id, username);
        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<List<GameSave>>> getUserGameSaves(@AuthenticationPrincipal Jwt jwt) {
        validateUser(jwt);
        String username = getUsernameFromJwt(jwt);

        List<GameSave> gameSaveList = gameSaveService.getGameSavesByUsername(username)
                .map(mapper::mapGameSaveEntityToGameSave)
                .toList();

        return generateResponse(HttpStatus.OK, gameSaveList);
    }
}
