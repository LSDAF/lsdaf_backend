package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.controllers.GoldController;
import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Gold;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.GoldService;
import com.lsadf.lsadf_backend.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of the Gold Controller
 */
@RestController
@Slf4j
public class GoldControllerImpl extends BaseController implements GoldController {

    private final GoldService goldService;
    private final Mapper mapper;

    public GoldControllerImpl(GoldService goldService,
                              Mapper mapper) {
        this.goldService = goldService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Gold>> getGold(@CurrentUser LocalUser localUser,
                                                         @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(localUser);

            String userEmail = localUser.getEmail();

            goldService.checkGameSaveOwnership(gameSaveId, userEmail);
            long gold = goldService.getGold(gameSaveId);
            Gold goldResponse = new Gold(gameSaveId, gold);

            return ResponseUtils.generateResponse(HttpStatus.OK, "Gold amount retrieved successfully.", goldResponse);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting gold amount: ", e);
            return ResponseUtils.generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while getting gold amount.", null);
        } catch (NotFoundException e) {
            log.error("Not found exception while getting gold amount: ", e);
            return ResponseUtils.generateResponse(HttpStatus.NOT_FOUND, "Not found exception while getting gold amount.", e.getMessage());
        } catch (Exception e) {
            log.error("Exception {} while getting gold amount: ", e.getClass(), e);
            return ResponseUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting gold amount.", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveGold(@CurrentUser LocalUser localUser,
                                                          @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                          @RequestParam(value = GOLD_AMOUNT) long amount) {
        try {
            validateUser(localUser);
            String userEmail = localUser.getEmail();

            goldService.checkGameSaveOwnership(gameSaveId, userEmail);
            goldService.saveGold(gameSaveId, amount, goldService.isCacheEnabled());

            return ResponseUtils.generateResponse(HttpStatus.OK, "Gold amount saved successfully.", null);
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
    protected Logger getLogger() {
        return log;
    }

}
