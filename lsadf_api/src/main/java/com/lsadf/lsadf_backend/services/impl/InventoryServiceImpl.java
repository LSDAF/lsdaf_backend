package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.InventoryEntity;
import com.lsadf.lsadf_backend.entities.ItemEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.repositories.InventoryRepository;
import com.lsadf.lsadf_backend.repositories.ItemRepository;
import com.lsadf.lsadf_backend.requests.item.ItemRequest;
import com.lsadf.lsadf_backend.services.InventoryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final Mapper mapper;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                ItemRepository itemRepository,
                                Mapper mapper) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryEntity getInventory(String gameSaveId) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }

        return getInventoryEntity(gameSaveId);
    }

    @Override
    public ItemEntity createItemInInventory(String gameSaveId, ItemRequest itemRequest) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }

        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);

        if (optionalInventoryEntity.isEmpty()) {
            throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
        }

        InventoryEntity inventoryEntity = optionalInventoryEntity.get();

        ItemEntity itemEntity = ItemEntity.builder()
                .inventoryEntity(inventoryEntity)
                .build();

        ItemEntity saved = itemRepository.save(itemEntity);

        inventoryEntity.getItems().add(saved);

        inventoryRepository.save(inventoryEntity);

        return saved;
    }

    @Override
    public void deleteItemFromInventory(String gameSaveId, String itemId) throws NotFoundException {
        if (gameSaveId == null || itemId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }

        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);

        if (optionalInventoryEntity.isEmpty()) {
            throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
        }

        InventoryEntity inventoryEntity = optionalInventoryEntity.get();

        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);

        if (optionalItemEntity.isEmpty()) {
            throw new NotFoundException("Item not found for item id " + itemId);
        }

        ItemEntity itemEntity = optionalItemEntity.get();

        inventoryEntity.getItems().remove(itemEntity);

        inventoryRepository.save(inventoryEntity);
    }

    @Override
    public ItemEntity updateItemInInventory(String gameSaveId, String itemId, ItemRequest itemRequest) throws NotFoundException {
        if (gameSaveId == null || itemId == null || itemRequest == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }

        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);

        if (optionalInventoryEntity.isEmpty()) {
            throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
        }

        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);

        if (optionalItemEntity.isEmpty()) {
            throw new NotFoundException("Item not found for item id " + itemId);
        }

        ItemEntity itemEntity = optionalItemEntity.get();

        // TODO: Update item

        itemRepository.save(itemEntity);

        return itemEntity;

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
