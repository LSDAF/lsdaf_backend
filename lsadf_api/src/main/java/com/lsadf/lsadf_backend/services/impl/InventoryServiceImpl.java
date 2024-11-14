package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.InventoryEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.repositories.InventoryRepository;
import com.lsadf.lsadf_backend.services.InventoryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final Mapper mapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                Mapper mapper) {
        this.inventoryRepository = inventoryRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventory(String gameSaveId) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }

        InventoryEntity inventoryEntity = getInventoryEntity(gameSaveId);

        return mapper.mapInventoryEntityToInventory(inventoryEntity);
    }

    @Override
    @Transactional
    public void saveInventory(String gameSaveId, Inventory inventory, boolean toCache) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }

        // TODO: Implement
    }

    /**
     * Get the inventory entity in the database or throw an exception if not found
     *
     * @param gameSaveId the game save id
     * @return the inventory entity
     * @throws NotFoundException if the inventory entity is not found
     */
    private InventoryEntity getInventoryEntity(String gameSaveId) throws NotFoundException {
        return inventoryRepository.findById(gameSaveId)
                .orElseThrow(() -> new NotFoundException("Inventory not found for game save id " + gameSaveId));
    }
}
