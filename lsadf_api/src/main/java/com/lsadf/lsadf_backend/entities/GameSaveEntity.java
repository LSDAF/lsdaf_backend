package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import com.lsadf.lsadf_backend.models.Characteristics;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.models.Stage;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Game Save Entity to persist data of a game save
 */
@Data
@Entity(name = EntityAttributes.GameSave.GAME_SAVE_ENTITY)
@Table(name = EntityAttributes.GameSave.GAME_SAVE_ENTITY)
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GameSaveEntity extends AEntity {

    @Serial
    private static final long serialVersionUID = 7786624859103259009L;

    protected GameSaveEntity() {
        super();
    }

    @Column(name = EntityAttributes.GameSave.GAME_SAVE_USER_EMAIL)
    private String userEmail;

    @Column(name = EntityAttributes.GameSave.GAME_SAVE_NICKNAME, unique = true)
    private String nickname;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = EntityAttributes.GameSave.GAME_SAVE_CHARACTERISTICS_ID)
    private CharacteristicsEntity characteristicsEntity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = EntityAttributes.GameSave.GAME_SAVE_CURRENCY_ID)
    private CurrencyEntity currencyEntity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = EntityAttributes.GameSave.GAME_SAVE_INVENTORY_ID)
    private InventoryEntity inventoryEntity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = EntityAttributes.GameSave.GAME_SAVE_STAGE_ID)
    private StageEntity stageEntity;

    /**
     * Set the characteristics of the game save
     * @param characteristicsEntity CharacteristicsEntity
     */
    public void setCharacteristicsEntity(CharacteristicsEntity characteristicsEntity) {
        this.characteristicsEntity = characteristicsEntity;
        characteristicsEntity.setGameSave(this);
    }

    /**
     * Set the currency of the game save
     * @param currencyEntity CurrencyEntity
     */
    public void setCurrencyEntity(CurrencyEntity currencyEntity) {
        this.currencyEntity = currencyEntity;
        currencyEntity.setGameSave(this);
    }

    /**
     * Set the inventory of the game save
     * @param inventoryEntity InventoryEntity
     */
    public void setInventoryEntity(InventoryEntity inventoryEntity) {
        this.inventoryEntity = inventoryEntity;
        inventoryEntity.setGameSave(this);
    }

    /**
     * Set the stage of the game save
     * @param stageEntity StageEntity
     */
    public void setStageEntity(StageEntity stageEntity) {
        this.stageEntity = stageEntity;
        stageEntity.setGameSave(this);
    }

    /**
     * Set the characteristics of the game save with a characteristics POJO
     * @param characteristics Characteristics
     */
    public void setCharacteristicsEntity(Characteristics characteristics) {
        if (characteristics.getAttack() != null) {
            this.characteristicsEntity.setAttack(characteristics.getAttack());
        }
        if (characteristics.getCritChance() != null) {
            this.characteristicsEntity.setCritChance(characteristics.getCritChance());
        }
        if (characteristics.getCritDamage() != null) {
            this.characteristicsEntity.setCritDamage(characteristics.getCritDamage());
        }
        if (characteristics.getHealth() != null) {
            this.characteristicsEntity.setHealth(characteristics.getHealth());
        }
        if (characteristics.getResistance() != null) {
            this.characteristicsEntity.setResistance(characteristics.getResistance());
        }
    }

    /**
     * Set the currency of the game save with a currency POJO
     * @param currency Currency
     */
    public void setCurrencyEntity(Currency currency) {
        if (currency.getGold() != null) {
            this.currencyEntity.setGoldAmount(currency.getGold());
        }
        if (currency.getDiamond() != null) {
            this.currencyEntity.setDiamondAmount(currency.getDiamond());
        }
        if (currency.getEmerald() != null) {
            this.currencyEntity.setEmeraldAmount(currency.getEmerald());
        }
        if (currency.getAmethyst() != null) {
            this.currencyEntity.setAmethystAmount(currency.getAmethyst());
        }
    }

    /**
     * Set the inventory of the game save with a inventory POJO
     * @param inventory Inventory
     */
    public void setInventoryEntity(Inventory inventory) {
        if (inventory.getItems() != null) {
            // TODO: get content of items
            Set<ItemEntity> items = inventory.getItems().stream().map(item -> new ItemEntity()).collect(Collectors.toSet());
            this.inventoryEntity.setItems(items);
        }
    }

    /**
     * Set the stage of the game save with a stage POJO
     * @param stage Stage
     */
    public void setStageEntity(Stage stage) {
        if (stage.getCurrentStage() != null) {
            this.stageEntity.setCurrentStage(stage.getCurrentStage());
        }
        if (stage.getMaxStage() != null) {
            this.stageEntity.setMaxStage(stage.getMaxStage());
        }
    }
}
