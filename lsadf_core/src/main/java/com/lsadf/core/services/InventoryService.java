package com.lsadf.core.services;

import com.lsadf.core.entities.InventoryEntity;
import com.lsadf.core.entities.ItemEntity;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.core.requests.item.ItemRequest;

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
     * @param itemId the item to add
     * @throws NotFoundException
     */
    void deleteItemFromInventory(String gameSaveId, String itemId) throws NotFoundException;

    /**
     * Update an item in the inventory of a game save
     * @param gameSaveId the game save id
     * @param itemId the item to update
     * @param itemRequest the item to update
     * @throws NotFoundException
     */
    ItemEntity updateItemInInventory(String gameSaveId, String itemId, ItemRequest itemRequest) throws NotFoundException;
}
