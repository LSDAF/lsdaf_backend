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

@Data
@Entity(name = EntityAttributes.Items.ITEM_ENTITY)
@Table(name = EntityAttributes.Items.ITEM_ENTITY)
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemEntity extends AEntity {

    @Serial
    private static final long serialVersionUID = 7924047722096464427L;

    protected ItemEntity() {
        super();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private InventoryEntity inventoryEntity;
}
