package com.lsadf.core.constants.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType {
  BOOTS("boots"),
  CHESTPLATE("chestplate"),
  GLOVES("gloves"),
  HELMET("helmet"),
  SHIELD("shield"),
  SWORD("sword");

  private final String type;

  public static ItemType fromString(String itemType) {
    for (ItemType itemTypeEnum : ItemType.values()) {
      if (itemTypeEnum.getType().equalsIgnoreCase(itemType)) {
        return itemTypeEnum;
      }
    }
    throw new IllegalArgumentException("Invalid item type");
  }
}
