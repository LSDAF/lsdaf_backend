package com.lsadf.core.services.impl;

import com.lsadf.core.constants.ItemType;
import com.lsadf.core.entities.InventoryEntity;
import com.lsadf.core.entities.ItemEntity;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.repositories.InventoryRepository;
import com.lsadf.core.repositories.ItemRepository;
import com.lsadf.core.requests.item.ItemRequest;
import com.lsadf.core.services.InventoryService;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;
  private final ItemRepository itemRepository;
  private final Mapper mapper;

  public InventoryServiceImpl(
      InventoryRepository inventoryRepository, ItemRepository itemRepository, Mapper mapper) {
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
  public ItemEntity createItemInInventory(String gameSaveId, ItemRequest itemRequest)
      throws NotFoundException {
    if (gameSaveId == null) {
      throw new IllegalArgumentException("Game save id cannot be null");
    }

    Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);

    if (optionalInventoryEntity.isEmpty()) {
      throw new NotFoundException("Inventory not found for game save id " + gameSaveId);
    }

    InventoryEntity inventoryEntity = optionalInventoryEntity.get();

    ItemEntity itemEntity =
        ItemEntity.builder()
            .inventoryEntity(inventoryEntity)
            .itemType(ItemType.fromString(itemRequest.getItemType()))
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
  public ItemEntity updateItemInInventory(String gameSaveId, String itemId, ItemRequest itemRequest)
      throws NotFoundException {
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

    itemEntity.setItemType(ItemType.fromString(itemRequest.getItemType()));

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
    return inventoryRepository
        .findById(gameSaveId)
        .orElseThrow(
            () -> new NotFoundException("Inventory not found for game save id " + gameSaveId));
  }
}
