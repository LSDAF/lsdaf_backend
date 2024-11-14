package com.lsadf.lsadf_backend.unit.services;

import com.lsadf.lsadf_backend.entities.InventoryEntity;
import com.lsadf.lsadf_backend.entities.ItemEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.impl.MapperImpl;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.models.Item;
import com.lsadf.lsadf_backend.repositories.InventoryRepository;
import com.lsadf.lsadf_backend.repositories.ItemRepository;
import com.lsadf.lsadf_backend.requests.item.ItemRequest;
import com.lsadf.lsadf_backend.services.InventoryService;
import com.lsadf.lsadf_backend.services.impl.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.MethodName.class)
class InventoryServiceTests {
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ItemRepository itemRepository;

    private final Mapper mapper = new MapperImpl();

    @BeforeEach
    public void init() {
        // Create all mocks and inject them into the service
        MockitoAnnotations.openMocks(this);

        inventoryService = new InventoryServiceImpl(inventoryRepository, itemRepository, mapper);
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

        // Assert
        assertThrows(NotFoundException.class, () -> inventoryService.getInventory("1"));
    }

    @Test
    void getInventory_on_existing_gamesave_id_with_empty_inventory() {
        // Arrange
        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new HashSet<>())
                .build();

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

        // Act
        InventoryEntity result = inventoryService.getInventory("1");

        // Assert
        assertThat(result).isEqualTo(inventoryEntity);
    }

    @Test
    void getInventory_on_existing_gamesave_id_with_items() {
        // Arrange
        ItemEntity itemEntity = ItemEntity.builder()
                .id("1")
                .build();

        ItemEntity itemEntity2 = ItemEntity.builder()
                .id("2")
                .build();

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new HashSet<>(List.of(itemEntity, itemEntity2)))
                .build();

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

        // Act
        InventoryEntity result = inventoryService.getInventory("1");


        // Assert
        assertThat(result).isEqualTo(inventoryEntity);
    }

    @Test
    void createItem_on_null_gamesave_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryService.createItem(null, new ItemRequest()));
    }

    @Test
    void createItem_on_non_existing_gamesave_id() {
        // Arrange
        when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(NotFoundException.class, () -> inventoryService.createItem("1", new ItemRequest()));
    }

    @Test
    void createItem_on_existing_gamesave_id() {
        // Arrange
        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new HashSet<>())
                .build();

        ItemRequest itemRequest = new ItemRequest();

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> inventoryService.createItem("1", itemRequest));
    }
}
