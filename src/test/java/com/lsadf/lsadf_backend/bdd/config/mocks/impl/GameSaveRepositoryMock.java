package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.repositories.GoldRepository;
import com.lsadf.lsadf_backend.services.GoldService;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

public class GameSaveRepositoryMock extends ARepositoryMock<GameSaveEntity> {

    private final GoldRepository goldRepository;

    public GameSaveRepositoryMock(GoldRepository goldRepository) {
        this.goldRepository = goldRepository;
    }

    @Override
    public GameSaveEntity save(GameSaveEntity entity) {
        Date now = new Date();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        GameSaveEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {

            // Save gold entity before saving game save entity
            GoldEntity goldEntity = saveGoldEntity(entity);

            entity.setGoldEntity(goldEntity);
            entity.setCreatedAt(goldEntity.getCreatedAt());
            entity.setUpdatedAt(goldEntity.getUpdatedAt());
            entities.put(entity.getId(), entity);
            return entity;
        }
        toUpdate.setAttack(entity.getAttack());
        toUpdate.setGoldEntity(entity.getGoldEntity());
        toUpdate.setHealthPoints(entity.getHealthPoints());
        toUpdate.setUpdatedAt(now);
        entities.put(entity.getId(), toUpdate);
        return toUpdate;
    }

    private GoldEntity saveGoldEntity(GameSaveEntity entity) {
        GoldEntity goldEntity = entity.getGoldEntity();
        goldEntity.setId(entity.getId());
        goldEntity.setGameSaveEntity(entity);
        return goldRepository.save(goldEntity);
    }

    public Stream<GameSaveEntity> findAllSaveGames() {
        return entities.values().stream();
    }
}
