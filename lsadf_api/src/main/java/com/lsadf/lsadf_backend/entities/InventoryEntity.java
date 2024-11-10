package com.lsadf.lsadf_backend.entities;

import com.lsadf.lsadf_backend.constants.EntityAttributes;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

@Data
@Entity(name = EntityAttributes.Inventory.INVENTORY_ENTITY)
@Table(name = EntityAttributes.Inventory.INVENTORY_ENTITY)
@SuperBuilder
@AllArgsConstructor
@ToString(callSuper = true)
public class InventoryEntity implements com.lsadf.lsadf_backend.entities.Entity {
    @Serial
    private static final long serialVersionUID = 7786624859103259009L;

    protected InventoryEntity() {
        super();
    }

    @Id
    @Column(name = EntityAttributes.ID)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private GameSaveEntity gameSave;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @MapsId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ItemEntity> items;
}
