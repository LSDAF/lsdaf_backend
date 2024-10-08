package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.Stage;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

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

    @ManyToOne
    @JoinColumn(name = EntityAttributes.User.USER_ID)
    private UserEntity user;

    @Builder.Default
    @Positive
    @Column(name = EntityAttributes.GameSave.GAME_SAVE_HEALTH_POINTS)
    private long healthPoints = 10L;

    @Builder.Default
    @PositiveOrZero
    @Column(name = EntityAttributes.GameSave.GAME_SAVE_ATTACK)
    private long attack = 1L;

    @Column(name = EntityAttributes.GameSave.GAME_SAVE_NICKNAME, unique = true)
    private String nickname;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = EntityAttributes.GameSave.GAME_SAVE_CURRENCY_ID)
    private CurrencyEntity currencyEntity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = EntityAttributes.GameSave.GAME_SAVE_STAGE_ID)
    private StageEntity stageEntity;

    /**
     * Set the user of the game save
     * @param currencyEntity CurrencyEntity
     */
    public void setCurrencyEntity(CurrencyEntity currencyEntity) {
        this.currencyEntity = currencyEntity;
        currencyEntity.setGameSave(this);
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
     * Set the currency of the game save with a currency POJO
     * @param currency Currency
     */
    public void setCurrencyEntity(Currency currency) {
        this.currencyEntity.setGoldAmount(currency.getGold());
        this.currencyEntity.setDiamondAmount(currency.getDiamond());
        this.currencyEntity.setEmeraldAmount(currency.getEmerald());
        this.currencyEntity.setAmethystAmount(currency.getAmethyst());
    }

    /**
     * Set the stage of the game save with a stage POJO
     * @param stage Stage
     */
    public void setStageEntity(Stage stage) {
        this.stageEntity.setCurrentStage(stage.getCurrentStage());
        this.stageEntity.setMaxStage(stage.getMaxStage());
    }
}
