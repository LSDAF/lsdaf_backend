package com.lsadf.lsadf_backend.unit.services;

import com.lsadf.core.constants.ItemType;
import com.lsadf.core.entities.InventoryEntity;
import com.lsadf.core.entities.ItemEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.impl.MapperImpl;
import com.lsadf.lsadf_backend.repositories.InventoryRepository;
import com.lsadf.lsadf_backend.repositories.ItemRepository;
import com.lsadf.lsadf_backend.requests.item.ItemRequest;
import com.lsadf.lsadf_backend.services.InventoryService;
import com.lsadf.lsadf_backend.services.impl.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
    void createItemInInventory_on_null_gamesave_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryService.createItemInInventory(null, new ItemRequest()));
    }

    @Test
    void createItemInInventory_on_non_existing_gamesave_id() {
        // Arrange
        when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(NotFoundException.class, () -> inventoryService.createItemInInventory("1", new ItemRequest()));
    }

    @Test
    void createItemInInventory_on_existing_gamesave_id_with_empty_inventory() {
        // Arrange
        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new HashSet<>())
                .build();

        ItemRequest itemRequest = new ItemRequest(ItemType.BOOTS.getType());

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

        // Act
        inventoryService.createItemInInventory("1", itemRequest);

        // Assert
        ArgumentCaptor<InventoryEntity> inventoryEntityCaptor = ArgumentCaptor.forClass(InventoryEntity.class);
        verify(inventoryRepository).save(inventoryEntityCaptor.capture());

        InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
        assertThat(capturedInventory.getItems()).hasSize(1);
    }

    @Test
    void createItemInInventory_on_existing_gamesave_id_with_non_empty_inventory() {
        // Arrange
        ItemEntity itemEntity = ItemEntity.builder().build();

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new HashSet<>(List.of(itemEntity)))
                .build();

        ItemRequest itemRequest = new ItemRequest(ItemType.BOOTS.getType());

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));

        // Act
        inventoryService.createItemInInventory("1", itemRequest);

        // Assert
        ArgumentCaptor<InventoryEntity> inventoryEntityCaptor = ArgumentCaptor.forClass(InventoryEntity.class);
        verify(inventoryRepository).save(inventoryEntityCaptor.capture());

        InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
        assertThat(capturedInventory.getItems()).hasSize(2);
    }

    @Test
    void deleteItemFromInventory_on_null_gamesave_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryService.deleteItemFromInventory(null, "1"));
    }

    @Test
    void deleteItemFromInventory_on_null_item_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryService.deleteItemFromInventory("1", null));
    }

    @Test
    void deleteItemFromInventory_on_non_existing_gamesave_id() {
        // Arrange
        when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(NotFoundException.class, () -> inventoryService.deleteItemFromInventory("1", "2"));
    }

    @Test
    void deleteItemFromInventory_on_non_existing_item_id() {
        // Arrange
        when(itemRepository.findById(anyString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(NotFoundException.class, () -> inventoryService.deleteItemFromInventory("1", "2"));
    }

    @Test
    void createItemInInventory_on_existing_gamesave_id_with_one_item_inventory() {
        // Arrange
        ItemEntity itemEntity = ItemEntity.builder()
                .id("2")
                .build();

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new HashSet<>(List.of(itemEntity)))
                .build();

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
        when(itemRepository.findById(anyString())).thenReturn(Optional.of(itemEntity));

        // Act
        inventoryService.deleteItemFromInventory("1", "2");

        // Assert
        ArgumentCaptor<InventoryEntity> inventoryEntityCaptor = ArgumentCaptor.forClass(InventoryEntity.class);
        verify(inventoryRepository).save(inventoryEntityCaptor.capture());

        InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
        assertThat(capturedInventory.getItems()).isEmpty();
    }

    @Test
    void createItemInInventory_on_existing_gamesave_id_with_two_items_inventory() {
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
        when(itemRepository.findById(anyString())).thenReturn(Optional.of(itemEntity2));

        // Act
        inventoryService.deleteItemFromInventory("1", "2");

        // Assert
        ArgumentCaptor<InventoryEntity> inventoryEntityCaptor = ArgumentCaptor.forClass(InventoryEntity.class);
        verify(inventoryRepository).save(inventoryEntityCaptor.capture());

        InventoryEntity capturedInventory = inventoryEntityCaptor.getValue();
        assertThat(capturedInventory.getItems()).hasSize(1);
    }

    @Test
    void updateItemInInventory_on_null_gamesave_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryService.updateItemInInventory(null, "1", new ItemRequest()));
    }

    @Test
    void updateItemInInventory_on_null_item_id() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryService.updateItemInInventory("1", null, new ItemRequest()));
    }

    @Test
    void updateItemInInventory_on_null_item_request() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryService.updateItemInInventory("1", "1", null));
    }

    @Test
    void updateItemInInventory_on_non_existing_gamesave_id() {
        // Arrange
        when(inventoryRepository.findById(anyString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(NotFoundException.class, () -> inventoryService.updateItemInInventory("1", "2", new ItemRequest()));
    }

    @Test
    void updateItemInInventory_on_non_existing_item_id() {
        // Arrange
        when(itemRepository.findById(anyString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(NotFoundException.class, () -> inventoryService.updateItemInInventory("1", "2", new ItemRequest()));
    }

    @Test
    void updateItemInInventory_on_existing_gamesave_id_with_one_item_inventory() {
        // Arrange
        ItemEntity itemEntity = ItemEntity.builder()
                .id("2")
                .itemType(ItemType.BOOTS)
                .build();

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .items(new HashSet<>(List.of(itemEntity)))
                .build();


        ItemRequest itemRequest = new ItemRequest(ItemType.SWORD.getType());

        when(inventoryRepository.findById(anyString())).thenReturn(Optional.of(inventoryEntity));
        when(itemRepository.findById(anyString())).thenReturn(Optional.of(itemEntity));

        // Act
        inventoryService.updateItemInInventory("1", "2", itemRequest);

        // Assert
        ArgumentCaptor<ItemEntity> itemEntityCaptor = ArgumentCaptor.forClass(ItemEntity.class);
        verify(itemRepository).save(itemEntityCaptor.capture());

        ItemEntity capturedItem = itemEntityCaptor.getValue();
        assertThat(capturedItem.getItemType()).isEqualTo(ItemType.SWORD);
    }
}
