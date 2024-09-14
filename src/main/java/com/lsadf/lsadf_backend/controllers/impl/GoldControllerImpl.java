package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.controllers.GoldController;
import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Gold;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.GoldService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

/**
 * Implementation of the Gold Controller
 */
@RestController
@Slf4j
public class GoldControllerImpl extends BaseController implements GoldController {

    private final GoldService goldService;
    private final GameSaveService gameSaveService;
    private final CacheService cacheService;
    private final Mapper mapper;

    public GoldControllerImpl(GoldService goldService,
                              GameSaveService gameSaveService,
                              CacheService cacheService,
                              Mapper mapper) {
        this.goldService = goldService;
        this.gameSaveService = gameSaveService;
        this.cacheService = cacheService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<GenericResponse<Gold>> getGold(@CurrentUser LocalUser localUser,
                                                         @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(localUser);

            String userEmail = localUser.getUsername();
            gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
            Gold goldResponse = new Gold(gameSaveId, goldService.getGold(gameSaveId));

            return generateResponse(HttpStatus.OK, goldResponse);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting gold amount: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while getting gold amount.", null);
        } catch (NotFoundException e) {
            log.error("Not found exception while getting gold amount: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, "Not found exception while getting gold amount: " + e.getMessage(), null);
        } catch (ForbiddenException e) {
            log.error("Forbidden exception while getting gold amount: ", e);
            return generateResponse(HttpStatus.FORBIDDEN, "Forbidden exception while getting gold amount: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception {} while getting gold amount: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting gold amount: " + e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ResponseEntity<GenericResponse<Void>> saveGold(@CurrentUser LocalUser localUser,
                                                          @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                          @RequestParam(value = GOLD_AMOUNT) long amount) {
        try {
            validateUser(localUser);
            String userEmail = localUser.getUsername();

            gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
            goldService.saveGold(gameSaveId, amount, cacheService.isEnabled());

            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while generating new save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while generating new save.", null);
        } catch (ForbiddenException e) {
            log.error("Forbidden exception while generating new save: ", e);
            return generateResponse(HttpStatus.FORBIDDEN, "Forbidden exception while generating new save: " + e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Not found exception while generating new save: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, "Not found exception while generating new save: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception {} while generating new save: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while generating new save: " + e.getMessage(), null);
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
