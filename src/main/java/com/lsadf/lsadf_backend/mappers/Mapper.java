package com.lsadf.lsadf_backend.mappers;

import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;

public interface Mapper {
    /**
     * Maps GameSaveEntity to GameSave
     *
     * @param gameSaveEntity GameSaveEntity
     * @return
     */
    GameSave mapToGameSave(GameSaveEntity gameSaveEntity);

    /**
     * Maps CurrencyRequest to Currency
     *
     * @param currencyRequest CurrencyRequest
     * @return Currency
     */
    Currency mapCurrencyRequestToCurrency(CurrencyRequest currencyRequest);

    /**
     * Maps Currency to CurrencyEntity
     * @param currencyEntity CurrencyEntity
     * @return CurrencyEntity
     */
    Currency mapCurrencyEntityToCurrency(CurrencyEntity currencyEntity);

    /**
     * Maps StageEntity to Stage
     * @param stageEntity StageEntity
     * @return Stage
     */
    Stage mapStageEntityToStage(StageEntity stageEntity);

    /**
     * Maps StageRequest to Stage
     * @param stageRequest StageRequest
     * @return Stage
     */
    Stage mapStageRequestToStage(StageRequest stageRequest);
}
