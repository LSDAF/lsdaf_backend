package com.lsadf.lsadf_backend.mappers;

import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import org.keycloak.representations.idm.UserRepresentation;

public interface Mapper {
    /**
     * Maps GameSaveEntity to GameSave
     *
     * @param gameSaveEntity GameSaveEntity
     * @return
     */
    GameSave mapGameSaveEntityToGameSave(GameSaveEntity gameSaveEntity);

    /**
     * Maps CurrencyRequest to Currency
     *
     * @param currencyRequest CurrencyRequest
     * @return Currency
     */
    Currency mapCurrencyRequestToCurrency(CurrencyRequest currencyRequest);

    /**
     * Maps Currency to CurrencyEntity
     *
     * @param currencyEntity CurrencyEntity
     * @return CurrencyEntity
     */
    Currency mapCurrencyEntityToCurrency(CurrencyEntity currencyEntity);

    /**
     * Maps StageEntity to Stage
     *
     * @param stageEntity StageEntity
     * @return Stage
     */
    Stage mapStageEntityToStage(StageEntity stageEntity);

    /**
     * Maps StageRequest to Stage
     *
     * @param stageRequest StageRequest
     * @return Stage
     */
    Stage mapStageRequestToStage(StageRequest stageRequest);

    /**
     * Maps Keycloak UserRepresentation to User
     *
     * @param userRepresentation UserRepresentation
     * @return User
     */
    User mapUserRepresentationToUser(UserRepresentation userRepresentation);

    /**
     * Maps UserCreationRequest to Keycloak UserRepresentation
     *
     * @param userCreationRequest UserCreationRequest
     * @return UserRepresentation
     */
    UserRepresentation mapUserCreationRequestToUserRepresentation(UserCreationRequest userCreationRequest);

    /**
     * Maps AdminUserCreationRequest to UserCreationRequest
     *
     * @param adminUserCreationRequest AdminUserCreationRequest
     * @return UserCreationRequest
     */
    UserCreationRequest mapAdminUserCreationRequestToUserCreationRequest(AdminUserCreationRequest adminUserCreationRequest);
}
