package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.services.CacheService;

public class NoOpCacheServiceImpl implements CacheService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toggleCacheEnabling() {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCaches() {
        // Do nothing
    }
}
