package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.InventoryController;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.requests.inventory.InventoryRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;
import static com.lsadf.lsadf_backend.utils.TokenUtils.getUsernameFromJwt;

/**
 * Implementation of the Inventory Controller
 */
@RestController
@Slf4j
public class InventoryControllerImpl extends BaseController implements InventoryController {

    private final GameSaveService gameSaveService;
    private final InventoryService inventoryService;
    private final CacheService cacheService;

    private final Mapper mapper;

    @Autowired
    public InventoryControllerImpl(GameSaveService gameSaveService,
                                   InventoryService inventoryService,
                                   CacheService cacheService,
                                   Mapper mapper) {
        this.gameSaveService = gameSaveService;
        this.inventoryService = inventoryService;
        this.cacheService = cacheService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> saveInventory(Jwt jwt,
                                                               String gameSaveId,
                                                               InventoryRequest inventoryRequest) {
        validateUser(jwt);
        String userEmail = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);

        Inventory inventory = mapper.mapInventoryRequestToInventory(inventoryRequest);
        inventoryService.saveInventory(gameSaveId, inventory, cacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> getInventory(Jwt jwt,
                                                              String gameSaveId) {
        validateUser(jwt);
        String userEmail = getUsernameFromJwt(jwt);
        gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
        Inventory inventory = inventoryService.getInventory(gameSaveId);
        return generateResponse(HttpStatus.OK, inventory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }
}
