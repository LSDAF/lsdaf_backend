package com.lsadf.controllers.impl;

import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.controllers.CurrencyController;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.Currency;
import com.lsadf.core.requests.currency.CurrencyRequest;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.services.CacheService;
import com.lsadf.core.services.CurrencyService;
import com.lsadf.core.services.GameSaveService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;
import static com.lsadf.core.utils.TokenUtils.getUsernameFromJwt;

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

    @Autowired
    public CurrencyControllerImpl(GameSaveService gameSaveService,
                                  CurrencyService currencyService,
                                  CacheService cacheService,
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
    public ResponseEntity<GenericResponse<Void>> saveCurrency(Jwt jwt,
                                                              String gameSaveId,
                                                              CurrencyRequest currencyRequest) {
        validateUser(jwt);
        String userEmail = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

        Currency currency = mapper.mapCurrencyRequestToCurrency(currencyRequest);
        currencyService.saveCurrency(gameSaveId, currency, cacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> getCurrency(Jwt jwt,
                                                             String gameSaveId) {
        validateUser(jwt);
        String userEmail = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
        Currency currency = currencyService.getCurrency(gameSaveId);
        return generateResponse(HttpStatus.OK, currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return log;
    }
}
