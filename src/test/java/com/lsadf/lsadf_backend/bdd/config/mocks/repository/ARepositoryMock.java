package com.lsadf.lsadf_backend.bdd.config.mocks.repository;

import com.lsadf.lsadf_backend.entities.Entity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

/**
 * Abstract class for repository mocks
 * @param <T> the entity type
 */
public abstract class ARepositoryMock<T extends Entity> implements CrudRepository<T, String> {
    protected final Map<String, T> entities = new HashMap<>();

    @Override
    public boolean existsById(@NotNull String id) {
        return entities.containsKey(id);
    }

    @Override
    public void deleteById(@NotNull String id) {
        entities.remove(id);
    }

    @Override
    public @NotNull Optional<T> findById(@NotNull String id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public long count() {
        return entities.size();
    }

    @Override
    public @NotNull Iterable<T> findAll() {
        return entities.values();
    }

    @Override
    public <S extends T> @NotNull Iterable<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public @NotNull Iterable<T> findAllById(@NotNull Iterable<String> strings) {
        List<T> result = new ArrayList<>();
        for (String id : strings) {
            T entity = entities.get(id);
            if (entity != null) {
                result.add(entity);
            }
        }

        return result;
    }

    @Override
    public void delete(@NotNull T entity) {
        entities.remove(entity.getId());
    }

    @Override
    public void deleteAllById(@NotNull Iterable<? extends String> strings) {
        for (String id : strings) {
            entities.remove(id);
        }
    }

    @Override
    public void deleteAll(@NotNull Iterable<? extends T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }
}
