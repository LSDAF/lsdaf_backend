package com.lsadf.lsadf_backend.bdd.config.mocks;

import java.util.List;
import java.util.Optional;

/**
 * Interface for repository mocks
 * @param <T> the entity type
 */
public interface RepositoryMock<T> {
    Optional<T> findById(String id);
    boolean existsById(String id);
    void deleteById(String id);
    Iterable<T> findAll();
    long count();
    T save(T entity);
    Iterable<T> saveAll(List<T> entities);
    void clear();
}
