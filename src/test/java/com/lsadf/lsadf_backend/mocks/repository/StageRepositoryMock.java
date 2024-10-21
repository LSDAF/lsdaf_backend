package com.lsadf.lsadf_backend.mocks.repository;

import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.repositories.StageRepository;

/**
 * Mock implementation of the StageRepository
 */
public class StageRepositoryMock extends ARepositoryMock<StageEntity> implements StageRepository {
    @Override
    public <S extends StageEntity> S save(S entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("The stage id must not be empty, since it should be the id of the associated game save");
        }

        entities.put(entity.getId(), entity);
        return entity;
    }
}
