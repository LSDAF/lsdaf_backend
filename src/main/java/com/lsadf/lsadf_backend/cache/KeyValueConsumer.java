package com.lsadf.lsadf_backend.cache;

public interface KeyValueConsumer<T> {
    void accept(String key, T value);
}
