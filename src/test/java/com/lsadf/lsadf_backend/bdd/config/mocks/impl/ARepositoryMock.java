package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.bdd.config.mocks.RepositoryMock;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract class for repository mocks
 * @param <T> the entity type
 */
public abstract class ARepositoryMock<T> implements RepositoryMock<T> {
    protected final Map<String, T> entities = new HashMap<>();

    @Override
    public boolean existsById(String id) {
        return entities.containsKey(id);
    }

    @Override
    public void deleteById(String id) {
        entities.remove(id);
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public long count() {
        return entities.size();
    }

    @Override
    public void clear() {
        entities.clear();
    }

    @Override
    public Iterable<T> findAll() {
        return entities.values();
    }

    @Override
    public Iterable<T> saveAll(List<T> entities) {
        return entities.stream().map(this::save).collect(Collectors.toList());
    }
}
