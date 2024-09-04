package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.entities.GoldEntity;

import java.util.Date;

public class GoldRepositoryMock extends ARepositoryMock<GoldEntity> {
    @Override
    public GoldEntity save(GoldEntity entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("The gold id must not be empty, since it should be the id of the associated game save");
        }

        entities.put(entity.getId(), entity);
        return entity;
    }
}
