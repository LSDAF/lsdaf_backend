package com.lsadf.lsadf_backend.cache;

import java.util.Map;

public interface HistoCache<T> extends Cache<T> {
    Map<String, T> getAllHisto();
}
