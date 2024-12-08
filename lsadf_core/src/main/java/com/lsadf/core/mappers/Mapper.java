package com.lsadf.core.mappers;

import com.lsadf.core.entities.*;
import com.lsadf.core.models.*;
import com.lsadf.core.requests.admin.AdminUserCreationRequest;
import com.lsadf.core.requests.characteristics.CharacteristicsRequest;
import com.lsadf.core.requests.currency.CurrencyRequest;
import com.lsadf.core.requests.stage.StageRequest;
import com.lsadf.core.requests.user.UserCreationRequest;
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
   * Maps CharacteristicsRequest to Characteristics
   *
   * @param characteristicsRequest CharacteristicsRequest
   * @return Characteristics
   */
  Characteristics mapCharacteristicsRequestToCharacteristics(
      CharacteristicsRequest characteristicsRequest);

  /**
   * Maps CharacteristicsEntity to Characteristics
   *
   * @param characteristicsEntity CharacteristicsEntity
   * @return Characteristics
   */
  Characteristics mapCharacteristicsEntityToCharacteristics(
      CharacteristicsEntity characteristicsEntity);

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
   * Maps Inventory to InventoryEntity
   *
   * @param inventoryEntity InventoryEntity
   * @return InventoryEntity
   */
  Inventory mapInventoryEntityToInventory(InventoryEntity inventoryEntity);

  /**
   * Maps ItemEntity to Item
   *
   * @param itemEntity ItemEntity
   * @return Item
   */
  Item mapItemEntityToItem(ItemEntity itemEntity);

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
  UserRepresentation mapUserCreationRequestToUserRepresentation(
      UserCreationRequest userCreationRequest);

  /**
   * Maps AdminUserCreationRequest to UserCreationRequest
   *
   * @param adminUserCreationRequest AdminUserCreationRequest
   * @return UserCreationRequest
   */
  UserCreationRequest mapAdminUserCreationRequestToUserCreationRequest(
      AdminUserCreationRequest adminUserCreationRequest);
}
