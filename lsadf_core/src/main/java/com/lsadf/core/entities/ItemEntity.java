package com.lsadf.core.entities;

import com.lsadf.core.constants.EntityAttributes;
import com.lsadf.core.constants.item.ItemRarity;
import com.lsadf.core.constants.item.ItemType;
import com.lsadf.core.models.ItemStat;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@Entity(name = EntityAttributes.Items.ITEM_ENTITY)
@Table(name = EntityAttributes.Items.ITEM_ENTITY)
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemEntity extends AEntity {

  @Serial private static final long serialVersionUID = 7924047722096464427L;

  protected ItemEntity() {
    super();
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private InventoryEntity inventoryEntity;

  @Column(name = EntityAttributes.Items.ITEM_CLIENT_ID, unique = true)
  private String clientId;

  @Column(name = EntityAttributes.Items.ITEM_BLUEPRINT_ID)
  private String blueprintId;

  @Column(name = EntityAttributes.Items.ITEM_TYPE)
  @Enumerated(EnumType.STRING)
  private ItemType itemType;

  @Column(name = EntityAttributes.Items.ITEM_RARITY)
  @Enumerated(EnumType.STRING)
  private ItemRarity itemRarity;

  @Column(name = EntityAttributes.Items.ITEM_IS_EQUIPPED)
  private Boolean isEquipped;

  @Column(name = EntityAttributes.Items.ITEM_LEVEL)
  @Positive
  private Integer level;

  @Column(name = EntityAttributes.Items.ITEM_MAIN_STAT)
  private ItemStat mainStat;

  @ElementCollection
  @CollectionTable(
      name = EntityAttributes.Items.ITEM_ADDITIONAL_STATS,
      joinColumns = @JoinColumn(name = "item_entity_id"))
  private List<ItemStat> additionalStats;
}
