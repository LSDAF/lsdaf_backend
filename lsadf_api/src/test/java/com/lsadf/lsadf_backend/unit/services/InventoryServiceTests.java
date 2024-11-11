package com.lsadf.lsadf_backend.unit.services;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.InventoryEntity;
import com.lsadf.lsadf_backend.entities.ItemEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.impl.MapperImpl;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.models.Item;
import com.lsadf.lsadf_backend.repositories.InventoryRepository;
import com.lsadf.lsadf_backend.services.InventoryService;
import com.lsadf.lsadf_backend.services.impl.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.MethodName.class)
class InventoryServiceTests {
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private Cache<Inventory> inventoryCache;

    private final Mapper mapper = new MapperImpl();

    @BeforeEach
    public void init() {
        // Create all mocks and inject them into the service
        MockitoAnnotations.openMocks(this);

        inventoryService = new InventoryServiceImpl(inventoryRepository, inventoryCache, mapper);
    }

    @Test
    void getInventory_on_null_gamesave_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryService.getInventory(null));
    }

    @Test
    void getInventory_on_non_existing_gamesave_id() {
        // Arrange
        when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());
        when(inventoryCache.isEnabled()).thenReturn(true);

        // Assert
        assertThrows(NotFoundException.class, () -> inventoryService.getInventory("1"));
    }

    @Test
    void getInventory_on_existing_gamesave_id_when_cached_empty() {
        // Arrange
        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new ArrayList<>())
                .build();

        Inventory inventory = Inventory.builder()
                .items(new ArrayList<>())
                .build();

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
        when(inventoryCache.isEnabled()).thenReturn(true);
        when(inventoryCache.get(anyString())).thenReturn(Optional.of(inventory));

        // Act
        Inventory result = inventoryService.getInventory("1");

        // Assert
        assertThat(result).isEqualTo(inventory);
    }

    @Test
    void getInventory_on_existing_gamesave_id_when_cached_not_empty() {
        // Arrange
        ItemEntity itemEntity = ItemEntity.builder()
                .build();

        Item item = Item.builder()
                .build();

        Item item2 = Item.builder()
                .build();

        ArrayList<ItemEntity> db_items = new ArrayList<>();
        db_items.add(itemEntity);

        ArrayList<Item> cache_items = new ArrayList<>();
        cache_items.add(item);
        cache_items.add(item2);

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(db_items)
                .build();

        Inventory inventory = Inventory.builder()
                .items(cache_items)
                .build();

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
        when(inventoryCache.isEnabled()).thenReturn(true);
        when(inventoryCache.get(anyString())).thenReturn(Optional.of(inventory));

        // Act
        Inventory result = inventoryService.getInventory("1");

        // Assert
        assertThat(result).isEqualTo(inventory);
    }

    @Test
    void getInventory_on_existing_gamesave_id_when_not_cached_empty() {
        // Arrange
        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new ArrayList<>())
                .build();

        Inventory inventory = Inventory.builder()
                .items(new ArrayList<>())
                .build();

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
        when(inventoryCache.isEnabled()).thenReturn(false);
        when(inventoryCache.get(anyString())).thenReturn(Optional.of(inventory));

        // Act
        Inventory result = inventoryService.getInventory("1");

        // Assert
        assertThat(result).isEqualTo(inventory);
    }

    @Test
    void getInventory_on_existing_gamesave_id_when_not_cached_not_empty() {
        // Arrange
        ItemEntity itemEntity = ItemEntity.builder()
                .build();

        Item item = Item.builder()
                .build();

        Item item2 = Item.builder()
                .build();

        ArrayList<ItemEntity> db_items = new ArrayList<>();
        db_items.add(itemEntity);

        ArrayList<Item> cache_items = new ArrayList<>();
        cache_items.add(item);
        cache_items.add(item2);

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(db_items)
                .build();

        Inventory inventory = Inventory.builder()
                .items(cache_items)
                .build();

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
        when(inventoryCache.isEnabled()).thenReturn(false);
        when(inventoryCache.get(anyString())).thenReturn(Optional.of(inventory));

        // Act
        Inventory result = inventoryService.getInventory("1");

        // Assert
        assertThat(result).isNotEqualTo(inventory);
        assertThat(result.getItems()).hasSize(1);
    }
}
