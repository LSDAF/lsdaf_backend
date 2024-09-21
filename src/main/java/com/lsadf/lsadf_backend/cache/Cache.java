package com.lsadf.lsadf_backend.cache;

import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;

public interface Cache<T> {
    Optional<T> get(String key);
    void set(String key, T value);
    Map<String, T> getAll();
    void clear();
    Logger getLogger();
    void setEnabled(boolean enabled);
    boolean isEnabled();
    void setExpiration(int expirationSeconds);
}
