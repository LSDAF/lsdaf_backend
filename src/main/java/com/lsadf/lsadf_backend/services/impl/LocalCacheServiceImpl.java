package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.models.LocalUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalCacheServiceImpl implements CacheService {

    private final Cache<LocalUser> localUserCache;
    private final Cache<String> invalidatedJwtTokenCache;

    public LocalCacheServiceImpl(Cache<LocalUser> localUserCache,
                                 Cache<String> invalidatedJwtTokenCache) {
        this.localUserCache = localUserCache;
        this.invalidatedJwtTokenCache = invalidatedJwtTokenCache;
    }

    @Override
    public Boolean isEnabled() {
        return true;
    }

    @Override
    public void toggleCacheEnabling() {
        // Do nothing
    }

    @Override
    public void clearCaches() {
        log.info("Cleaning local caches");
        localUserCache.clear();
        invalidatedJwtTokenCache.clear();
        log.info("Local caches cleaned");
    }
}
