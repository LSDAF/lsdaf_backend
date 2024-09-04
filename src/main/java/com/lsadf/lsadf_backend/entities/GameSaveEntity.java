package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serial;

/**
 * Game Save Entity to persist data of a game save
 */
@Data
@Entity(name = EntityAttributes.GameSave.GAME_SAVE_ENTITY)
@Table(name = EntityAttributes.GameSave.GAME_SAVE_ENTITY)
@Builder
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = EntityAttributes.GameSave.GAME_SAVE_GOLD_ID)
    @ToString.Exclude
    private GoldEntity goldEntity;

    public void setGoldEntity(GoldEntity goldEntity) {
        this.goldEntity = goldEntity;
        goldEntity.setGameSave(this);
    }
}
