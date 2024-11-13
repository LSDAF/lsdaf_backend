package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.StageController;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.StageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;
import static com.lsadf.lsadf_backend.utils.TokenUtils.getUsernameFromJwt;

/**
 * Implementation of the Stage Controller
 */
@RestController
@Slf4j
public class StageControllerImpl extends BaseController implements StageController {

    private final GameSaveService gameSaveService;
    private final CacheService cacheService;
    private final Mapper mapper;
    private final StageService stageService;

    @Autowired
    public StageControllerImpl(GameSaveService gameSaveService,
                               CacheService cacheService,
                               Mapper mapper,
                               StageService stageService) {
        this.gameSaveService = gameSaveService;
        this.cacheService = cacheService;
        this.mapper = mapper;
        this.stageService = stageService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveStage(Jwt jwt,
                                                           String gameSaveId,
                                                           StageRequest stageRequest) {
        validateUser(jwt);
        String username = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(gameSaveId, username);

        Stage stage = mapper.mapStageRequestToStage(stageRequest);
        stageService.saveStage(gameSaveId, stage, cacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> getStage(Jwt jwt,
                                                          String gameSaveId) {
        validateUser(jwt);
        String username = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(gameSaveId, username);
        Stage stage = stageService.getStage(gameSaveId);
        return generateResponse(HttpStatus.OK, stage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }
}
