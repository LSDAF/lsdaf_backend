package com.lsadf.lsadf_backend.controllers.admin.impl;

import com.lsadf.lsadf_backend.controllers.admin.AdminCacheController;
import com.lsadf.lsadf_backend.controllers.impl.BaseController;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.services.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

/**
 * The implementation of the AdminCacheController
 */
@RestController
@Slf4j
public class AdminCacheControllerImpl extends BaseController implements AdminCacheController {

    private final CacheService redisCacheService;
    private final CacheFlushService cacheFlushService;

    @Autowired
    public AdminCacheControllerImpl(CacheService redisCacheService,
                                    CacheFlushService cacheFlushService) {
        this.redisCacheService = redisCacheService;
        this.cacheFlushService = cacheFlushService;
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
    public ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(Jwt jwt) {
        validateUser(jwt);
        boolean cacheEnabled = redisCacheService.isEnabled();
        return generateResponse(HttpStatus.OK, cacheEnabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Boolean>> toggleRedisCacheEnabling(Jwt jwt) {
        validateUser(jwt);
        redisCacheService.toggleCacheEnabling();
        Boolean cacheEnabled = redisCacheService.isEnabled();
        return generateResponse(HttpStatus.OK, cacheEnabled);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> flushAndClearCache(Jwt jwt) {
        validateUser(jwt);

        log.info("Clearing all caches");
        cacheFlushService.flushCharacteristics();
        cacheFlushService.flushCurrencies();
        cacheFlushService.flushInventories();
        cacheFlushService.flushStages();
        redisCacheService.clearCaches();

        return generateResponse(HttpStatus.OK);
    }
}
