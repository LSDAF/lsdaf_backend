package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.*;
import lombok.*;

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
    protected GameSaveEntity() {
        super();
    }

    @ManyToOne
    @JoinColumn(name = EntityAttributes.User.USER_ID)
    private UserEntity user;

    @Builder.Default
    private long gold = 0L;

    @Builder.Default
    private long healthPoints = 10L;

    @Builder.Default
    private long attack = 1L;
}
