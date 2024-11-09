package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.HistoCache;

import java.util.Map;

public class NoOpHistoCache<T> extends NoOpCache<T> implements HistoCache<T> {
    @Override
    public Map<String, T> getAllHisto() {
        return Map.of();
    }

}
