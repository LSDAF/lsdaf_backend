package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

public class GameSaveRepositoryMock extends ARepositoryMock<GameSaveEntity> {
    @Override
    public GameSaveEntity save(GameSaveEntity entity) {
        Date now = new Date();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        GameSaveEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            entities.put(entity.getId(), entity);
            return entity;
        }
        toUpdate.setAttack(entity.getAttack());
        toUpdate.setGold(entity.getGold());
        toUpdate.setHealthPoints(entity.getHealthPoints());
        toUpdate.setUpdatedAt(now);
        entities.put(entity.getId(), toUpdate);
        return toUpdate;
    }


    public Stream<GameSaveEntity> findAllSaveGames() {
        return entities.values().stream();
    }
}
