package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.InventoryEntity;
import com.lsadf.lsadf_backend.entities.ItemEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.requests.item.ItemRequest;

public interface InventoryService {

    /**
     * Get the inventory of a game save
     * @param gameSaveId the id of the game save
     * @return the inventory entity
     */
    InventoryEntity getInventory(String gameSaveId);

    /**
     * Create an item in the inventory of a game save
     * @param gameSaveId the game save id
     * @param itemRequest the item to add
     * @throws NotFoundException
     */
    ItemEntity createItemInInventory(String gameSaveId, ItemRequest itemRequest) throws NotFoundException;

    /**
     * Remove an item from the inventory of a game save
     * @param gameSaveId the game save id
     * @param itemRequest the item to add
     * @throws NotFoundException
     */
    void deleteItemFromInventory(String gameSaveId, String itemId) throws NotFoundException;
}
