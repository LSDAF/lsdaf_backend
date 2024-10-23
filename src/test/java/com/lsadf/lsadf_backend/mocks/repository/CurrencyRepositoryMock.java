package com.lsadf.lsadf_backend.mocks.repository;

import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import org.jetbrains.annotations.NotNull;

/**
 * Mock implementation of the GoldRepository
 */
public class CurrencyRepositoryMock extends ARepositoryMock<CurrencyEntity> implements CurrencyRepository {

    @Override
    public <S extends CurrencyEntity> @NotNull S save(S entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("The currency id must not be empty, since it should be the id of the associated game save");
        }

        entities.put(entity.getId(), entity);
        return entity;
    }
}
