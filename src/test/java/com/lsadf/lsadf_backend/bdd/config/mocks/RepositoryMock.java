package com.lsadf.lsadf_backend.bdd.config.mocks;

import java.util.List;
import java.util.Optional;

public interface RepositoryMock<T> {
    Optional<T> findById(String id);
    Iterable<T> findAll();
    long count();
    T save(T entity);
    Iterable<T> saveAll(List<T> entities);
    void clear();
}
