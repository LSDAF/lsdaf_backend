package com.lsadf.core.cache;

import java.util.Map;

public interface HistoCache<T> extends Cache<T> {
  Map<String, T> getAllHisto();
}
