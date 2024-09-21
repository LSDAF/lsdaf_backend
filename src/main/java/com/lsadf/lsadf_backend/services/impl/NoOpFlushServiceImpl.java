package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.services.CacheFlushService;

public class NoOpFlushServiceImpl implements CacheFlushService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void flushCurrencies() {
        // Do nothing
    }
}
