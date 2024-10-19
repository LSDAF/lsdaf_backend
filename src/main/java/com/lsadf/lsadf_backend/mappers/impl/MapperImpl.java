package com.lsadf.lsadf_backend.mappers.impl;

import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MapperImpl implements Mapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency mapCurrencyRequestToCurrency(CurrencyRequest currencyRequest) {
        return new Currency(currencyRequest.getGold(),
                currencyRequest.getDiamond(),
                currencyRequest.getEmerald(),
                currencyRequest.getAmethyst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSave mapToGameSave(GameSaveEntity gameSaveEntity) {
        Stage stage = mapStageEntityToStage(gameSaveEntity.getStageEntity());
        Currency currency = mapCurrencyEntityToCurrency(gameSaveEntity.getCurrencyEntity());

        return GameSave.builder()
                .id(gameSaveEntity.getId())
                .userEmail(gameSaveEntity.getUserEmail())
                .nickname(gameSaveEntity.getNickname())
                .currency(currency)
                .stage(stage)
                .healthPoints(gameSaveEntity.getHealthPoints())
                .attack(gameSaveEntity.getAttack())
                .id(gameSaveEntity.getId())
                .createdAt(gameSaveEntity.getCreatedAt())
                .updatedAt(gameSaveEntity.getUpdatedAt())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Currency mapCurrencyEntityToCurrency(CurrencyEntity currencyEntity) {
        return new Currency(currencyEntity.getGoldAmount(),
                currencyEntity.getDiamondAmount(),
                currencyEntity.getEmeraldAmount(),
                currencyEntity.getAmethystAmount());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stage mapStageEntityToStage(StageEntity stageEntity) {
        return Stage.builder()
                .maxStage(stageEntity.getMaxStage())
                .currentStage(stageEntity.getCurrentStage())
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Stage mapStageRequestToStage(StageRequest stageRequest) {
        return Stage.builder()
                .maxStage(stageRequest.getMaxStage())
                .currentStage(stageRequest.getCurrentStage())
                .build();
    }
}
