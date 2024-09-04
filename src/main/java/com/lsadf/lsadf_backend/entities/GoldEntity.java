package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity(name = EntityAttributes.Gold.GOLD_ENTITY)
@Table(name = EntityAttributes.Gold.GOLD_ENTITY)
@Builder
@AllArgsConstructor
@ToString(callSuper = true)
public class GoldEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7786624859103259009L;

    protected GoldEntity() {
        super();
    }

    @Id
    @Column(name = EntityAttributes.ID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @ToString.Exclude
    private GameSaveEntity gameSave;

    @Column(name = EntityAttributes.Gold.GOLD_USER_EMAIL)
    private String userEmail;

    @Column(name = EntityAttributes.Gold.GOLD_AMOUNT)
    @PositiveOrZero
    @Builder.Default
    private long goldAmount = 0L;

}
