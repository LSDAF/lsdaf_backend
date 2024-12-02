package com.lsadf.lsadf_backend.controllers.impl;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;
import static com.lsadf.core.utils.TokenUtils.getUsernameFromJwt;

import com.lsadf.core.controllers.impl.BaseController;
import com.lsadf.core.entities.InventoryEntity;
import com.lsadf.core.entities.ItemEntity;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.Inventory;
import com.lsadf.core.requests.item.ItemRequest;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.services.CacheService;
import com.lsadf.core.services.GameSaveService;
import com.lsadf.core.services.InventoryService;
import com.lsadf.lsadf_backend.controllers.InventoryController;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of the Inventory Controller */
@RestController
@Slf4j
public class InventoryControllerImpl extends BaseController implements InventoryController {

  private final GameSaveService gameSaveService;
  private final InventoryService inventoryService;
  private final CacheService cacheService;

  private final Mapper mapper;

  @Autowired
  public InventoryControllerImpl(
      GameSaveService gameSaveService,
      InventoryService inventoryService,
      CacheService cacheService,
      Mapper mapper) {
    this.gameSaveService = gameSaveService;
    this.inventoryService = inventoryService;
    this.cacheService = cacheService;
    this.mapper = mapper;
  }

  /** {@inheritDoc} */
  @Override
  public ResponseEntity<GenericResponse<Void>> getInventory(Jwt jwt, String gameSaveId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    InventoryEntity inventoryEntity = inventoryService.getInventory(gameSaveId);
    Inventory inventory = mapper.mapInventoryEntityToInventory(inventoryEntity);
    return generateResponse(HttpStatus.OK, inventory);
  }

  /**
   * {@inheritDoc
   */
  @Override
  public ResponseEntity<GenericResponse<Void>> createItemInInventory(
      Jwt jwt, String gameSaveId, ItemRequest itemRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    ItemEntity itemEntity = inventoryService.createItemInInventory(gameSaveId, itemRequest);
    return generateResponse(HttpStatus.OK, itemEntity);
  }

  /**
   * {@inheritDoc
   */
  @Override
  public ResponseEntity<GenericResponse<Void>> deleteItemFromInventory(
      Jwt jwt, String gameSaveId, String itemId) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    inventoryService.deleteItemFromInventory(gameSaveId, itemId);
    return generateResponse(HttpStatus.OK);
  }

  /**
   * {@inheritDoc
   */
  @Override
  public ResponseEntity<GenericResponse<Void>> updateItemInInventory(
      Jwt jwt, String gameSaveId, String itemId, ItemRequest itemRequest) {
    validateUser(jwt);
    String userEmail = getUsernameFromJwt(jwt);
    gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
    inventoryService.updateItemInInventory(gameSaveId, itemId, itemRequest);
    return generateResponse(HttpStatus.OK);
  }

  /** {@inheritDoc} */
  @Override
  public Logger getLogger() {
    return log;
  }
}
