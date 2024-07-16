package com.lsadf.lsadf_backend.bdd.config.mocks;

import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ARepositoryMock<T> implements RepositoryMock<T> {
    protected final Map<String, T> entities = new HashMap<>();

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
