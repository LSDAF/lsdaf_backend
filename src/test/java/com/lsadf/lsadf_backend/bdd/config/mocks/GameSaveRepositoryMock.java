package com.lsadf.lsadf_backend.bdd.config.mocks;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Mock implementation of the GameSaveRepository
 */
public class GameSaveRepositoryMock extends ARepositoryMock<GameSaveEntity> implements GameSaveRepository {

    private final CurrencyRepository currencyRepository;

    public GameSaveRepositoryMock(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void deleteById(@NotNull String id) {
        super.deleteById(id);
        currencyRepository.deleteById(id);
    }

    @Override
    public Stream<GameSaveEntity> findAllGameSaves() {
        return entities.values().stream();
    }

    @Override
    public <S extends GameSaveEntity> @NotNull S save(S entity) {
        Date now = new Date();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        GameSaveEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
            entities.put(entity.getId(), entity);
            CurrencyEntity currencyEntity = entity.getCurrencyEntity();
            if (currencyEntity != null) {
                currencyRepository.save(currencyEntity);
            }
            return entity;
        }
        toUpdate.setAttack(entity.getAttack());
        toUpdate.setHealthPoints(entity.getHealthPoints());
        toUpdate.setUpdatedAt(now);

        CurrencyEntity currencyEntity = entity.getCurrencyEntity();
        CurrencyEntity updatedCurrencyEntity = currencyRepository.save(currencyEntity);

        toUpdate.setCurrencyEntity(updatedCurrencyEntity);
        entities.put(entity.getId(), toUpdate);

        return (S) toUpdate;
    }

    @Override
    public List<GameSaveEntity> findGameSaveEntitiesByUserEmail(String userEmail) {
        return entities.values()
                .stream()
                .filter(gameSaveEntity -> gameSaveEntity.getUser().getEmail().equals(userEmail))
                .toList();
    }
}
