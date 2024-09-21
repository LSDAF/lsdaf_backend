package com.lsadf.lsadf_backend.utils;

@FunctionalInterface
public interface KeyValueConsumer<T> {
    void accept(String key, T value);
}
