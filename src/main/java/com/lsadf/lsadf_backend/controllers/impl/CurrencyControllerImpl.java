package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.annotations.CurrentUser;
import com.lsadf.lsadf_backend.controllers.CurrencyController;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.REDIS_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

/**
 * Implementation of the Currency Controller
 */
@RestController
@Slf4j
public class CurrencyControllerImpl extends BaseController implements CurrencyController {

    private final GameSaveService gameSaveService;
    private final CurrencyService currencyService;
    private final CacheService cacheService;

    private final Mapper mapper;

    public CurrencyControllerImpl(GameSaveService gameSaveService,
                                  CurrencyService currencyService,
                                  @Qualifier(REDIS_CACHE_SERVICE) CacheService cacheService,
                                  Mapper mapper) {
        this.gameSaveService = gameSaveService;
        this.currencyService = currencyService;
        this.cacheService = cacheService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveCurrency(@CurrentUser LocalUser localUser,
                                                              @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                              @RequestBody @Valid CurrencyRequest currencyRequest) {
        try {
            validateUser(localUser);
            String userEmail = localUser.getUsername();
            gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

            boolean hasUpdates = false;
            Currency currency = mapper.mapCurrencyRequestToCurrency(currencyRequest);
            currencyService.saveCurrency(gameSaveId, currency, cacheService.isEnabled());

            return generateResponse(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument exception while saving currency: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, "No currency to save: " + e.getMessage(), null);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while saving currency: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while saving currency.", null);
        } catch (ForbiddenException e) {
            log.error("Forbidden exception while saving currency: ", e);
            return generateResponse(HttpStatus.FORBIDDEN, "Forbidden exception while saving currency.", null);
        } catch (NotFoundException e) {
            log.error("Not found exception while saving currency: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, "Not found exception while saving currency: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception {} while saving currency: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while saving currency: " + e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> getCurrency(@CurrentUser LocalUser localUser,
                                                             @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(localUser);
            String userEmail = localUser.getUsername();
            gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
            Currency currency = currencyService.getCurrency(gameSaveId);
            return generateResponse(HttpStatus.OK, currency);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting currency: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, "Unauthorized exception while getting currency.", null);
        } catch (ForbiddenException e) {
            log.error("Forbidden exception while getting currency: ", e);
            return generateResponse(HttpStatus.FORBIDDEN, "Forbidden exception while getting currency.", null);
        } catch (NotFoundException e) {
            log.error("Not found exception while getting currency: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, "Not found exception while getting currency: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception {} while getting currency: ", e.getClass(), e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Exception " + e.getClass() + " while getting currency: " + e.getMessage(), null);
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
