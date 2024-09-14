package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.repositories.GoldRepository;
import com.lsadf.lsadf_backend.services.GoldService;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Mock implementation of the GameSaveRepository
 */
public class GameSaveRepositoryMock extends ARepositoryMock<GameSaveEntity> {

    private final GoldRepository goldRepository;

    public GameSaveRepositoryMock(GoldRepository goldRepository) {
        this.goldRepository = goldRepository;
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(id);
        goldRepository.deleteById(id);
    }

    @Override
    public GameSaveEntity save(GameSaveEntity entity) {
        Date now = new Date();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        GameSaveEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
            entities.put(entity.getId(), entity);
            GoldEntity goldEntity = entity.getGoldEntity();
            if (goldEntity != null) {
                goldRepository.save(goldEntity);
            }
            return entity;
        }
        toUpdate.setAttack(entity.getAttack());
        toUpdate.setHealthPoints(entity.getHealthPoints());
        toUpdate.setUpdatedAt(now);

        var gold = entity.getGoldEntity();
        var updatedGold = goldRepository.save(gold);

        toUpdate.setGoldEntity(updatedGold);
        entities.put(entity.getId(), toUpdate);

        return toUpdate;
    }

    public Stream<GameSaveEntity> findAllSaveGames() {
        return entities.values().stream();
    }

    public List<GameSaveEntity> findGameSaveEntitiesByUserEmail(String userId) {
        return entities.values().stream().filter(gameSaveEntity -> gameSaveEntity.getUser().getEmail().equals(userId)).toList();
    }
}
