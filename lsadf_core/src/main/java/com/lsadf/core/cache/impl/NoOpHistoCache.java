package com.lsadf.core.cache.impl;

import com.lsadf.core.cache.HistoCache;

import java.util.Map;

public class NoOpHistoCache<T> extends NoOpCache<T> implements HistoCache<T> {
    @Override
    public Map<String, T> getAllHisto() {
        return Map.of();
    }

}
