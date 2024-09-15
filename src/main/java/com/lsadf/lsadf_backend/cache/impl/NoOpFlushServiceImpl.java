package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheFlushService;

public class NoOpFlushServiceImpl implements CacheFlushService {
    @Override
    public void flushCurrencies() {
        // Do nothing
    }
}
