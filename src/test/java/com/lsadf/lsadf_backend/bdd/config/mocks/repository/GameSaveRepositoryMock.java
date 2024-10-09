package com.lsadf.lsadf_backend.bdd.config.mocks.repository;

import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.StageRepository;
import com.lsadf.lsadf_backend.services.ClockService;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Mock implementation of the GameSaveRepository
 */
public class GameSaveRepositoryMock extends ARepositoryMock<GameSaveEntity> implements GameSaveRepository {

    private final CurrencyRepository currencyRepository;
    private final StageRepository stageRepository;
    private final ClockService clockService;

    public GameSaveRepositoryMock(CurrencyRepository currencyRepository,
                                  StageRepository stageRepository,
                                  ClockService clockService) {
        this.currencyRepository = currencyRepository;
        this.stageRepository = stageRepository;
        this.clockService = clockService;
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
        Date now = clockService.nowDate();
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
            StageEntity stageEntity = entity.getStageEntity();
            if (stageEntity != null) {
                stageRepository.save(stageEntity);
            }
            return entity;
        }
        toUpdate.setAttack(entity.getAttack());
        toUpdate.setHealthPoints(entity.getHealthPoints());
        toUpdate.setUpdatedAt(now);

        CurrencyEntity currencyEntity = entity.getCurrencyEntity();
        CurrencyEntity updatedCurrencyEntity = currencyRepository.save(currencyEntity);
        toUpdate.setCurrencyEntity(updatedCurrencyEntity);

        StageEntity stageEntity = entity.getStageEntity();
        StageEntity updatedStageEntity = stageRepository.save(stageEntity);
        toUpdate.setStageEntity(updatedStageEntity);

        entities.put(entity.getId(), toUpdate);

        return (S) toUpdate;
    }

    @Override
    public Stream<GameSaveEntity> findGameSaveEntitiesByUserEmail(String userEmail) {
        return entities.values()
                .stream()
                .filter(gameSaveEntity -> gameSaveEntity.getUser().getEmail().equals(userEmail));
    }

    /**
     * {@inheritDoc}
     * @param nickname the nickname
     * @return the game save entity by nickname or empty
     */
    @Override
    public Optional<GameSaveEntity> findGameSaveEntityByNickname(String nickname) {
        return entities.values()
                .stream()
                .filter(gameSaveEntity -> gameSaveEntity.getNickname().equals(nickname))
                .findAny();
    }
}
