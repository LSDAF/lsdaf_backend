package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.repositories.InventoryRepository;
import com.lsadf.lsadf_backend.services.InventoryService;
import org.springframework.transaction.annotation.Transactional;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final Cache<Inventory> inventoryCache;
    private final Mapper mapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                Cache<Inventory> inventoryCache,
                                Mapper mapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryCache = inventoryCache;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventory(String gameSaveId) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }

        // TODO: Implement
        return null;
    }

    @Override
    @Transactional
    public void saveInventory(String gameSaveId, Inventory inventory, boolean toCache) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }

        // TODO: Implement
    }
}
