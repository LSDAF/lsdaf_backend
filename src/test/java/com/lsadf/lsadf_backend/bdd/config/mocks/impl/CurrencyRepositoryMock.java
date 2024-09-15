package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.entities.CurrencyEntity;

/**
 * Mock implementation of the GoldRepository
 */
public class CurrencyRepositoryMock extends ARepositoryMock<CurrencyEntity> {
    @Override
    public CurrencyEntity save(CurrencyEntity entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("The gold id must not be empty, since it should be the id of the associated game save");
        }

        entities.put(entity.getId(), entity);
        return entity;
    }
}
