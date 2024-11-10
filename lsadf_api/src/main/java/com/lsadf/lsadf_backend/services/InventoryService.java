package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.Inventory;

public interface InventoryService {

    /**
     * Get the inventory of a game save
     * @param gameSaveId the id of the game save
     * @return the inventory POJO
     */
    Inventory getInventory(String gameSaveId);

    /**
     * Save the inventory of a game save
     * @param gameSaveId the id of the game save
     * @param inventory the inventory POJO
     * @param toCache true if the inventory should be saved to cache, false otherwise
     * @throws NotFoundException
     */
    void saveInventory(String gameSaveId, Inventory inventory, boolean toCache) throws NotFoundException;
}
