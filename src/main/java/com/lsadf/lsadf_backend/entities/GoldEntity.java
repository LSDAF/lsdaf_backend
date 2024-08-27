package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serial;

@Data
@Entity(name = EntityAttributes.Gold.GOLD_ENTITY)
@Table(name = EntityAttributes.Gold.GOLD_ENTITY)
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GoldEntity extends AEntity {

    @Serial
    private static final long serialVersionUID = 7786624859103259009L;

    protected GoldEntity() {
        super();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @ToString.Exclude
    private GameSaveEntity gameSaveEntity;

    @Column(name = EntityAttributes.Gold.GOLD_USER_ID)
    private String userEmail;

    @Column(name = EntityAttributes.Gold.GOLD_AMOUNT)
    @PositiveOrZero
    @Builder.Default
    private long goldAmount = 0L;


}
