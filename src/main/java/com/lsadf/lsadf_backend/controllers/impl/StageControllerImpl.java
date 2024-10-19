package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.StageController;
import com.lsadf.lsadf_backend.exceptions.http.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.http.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.StageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.REDIS_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

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
                               @Qualifier(REDIS_CACHE_SERVICE) CacheService cacheService,
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
    public ResponseEntity<GenericResponse<Void>> saveStage(@AuthenticationPrincipal Jwt jwt,
                                                           @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                           @Valid @RequestBody StageRequest stageRequest) {
        try {
            validateUser(jwt);
            String userEmail = jwt.getSubject();
            gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

            Stage stage = mapper.mapStageRequestToStage(stageRequest);
            stageService.saveStage(gameSaveId, stage, cacheService.isEnabled());

            return generateResponse(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument exception while saving stage: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, "Illegal argument while saving stage: " + e.getMessage(), null);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while saving stage: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized while saving stage: " + e.getMessage(), null);
        } catch (ForbiddenException e) {
            log.error("Forbidden exception while saving stage: ", e);
            return generateResponse(HttpStatus.FORBIDDEN, "Forbidden while saving stage: " + e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Not found exception while saving stage: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, "Not found while saving stage: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception while saving stage: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception while saving stage: " + e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> getStage(@AuthenticationPrincipal Jwt jwt,
                                                          @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(jwt);
            String userEmail = jwt.getSubject();
            gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
            Stage stage = stageService.getStage(gameSaveId);
            return generateResponse(HttpStatus.OK, stage);
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument exception while getting stage: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, "Illegal argument while getting stage: " + e.getMessage(), null);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting stage: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized while getting stage: " + e.getMessage(), null);
        } catch (ForbiddenException e) {
            log.error("Forbidden exception while getting stage: ", e);
            return generateResponse(HttpStatus.FORBIDDEN, "Forbidden while getting stage: " + e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Not found exception while getting stage: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, "Not found while getting stage: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception while getting stage: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception while getting stage: " + e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }
}
