package com.lsadf.core.utils;

@FunctionalInterface
public interface KeyValueConsumer<T> {
    void accept(String key, T value);
}
