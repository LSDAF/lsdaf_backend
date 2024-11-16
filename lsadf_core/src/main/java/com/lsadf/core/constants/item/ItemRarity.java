package com.lsadf.core.constants.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemRarity {
  NORMAL("normal"),
  RARE("rare"),
  MAGIC("magic"),
  EPIC("epic"),
  LEGENDARY("legendary"),
  MYTHIC("mythic");

  private final String rarity;

  public static ItemRarity fromString(String itemRarity) {
    for (ItemRarity itemRarityEnum : ItemRarity.values()) {
      if (itemRarityEnum.getRarity().equalsIgnoreCase(itemRarity)) {
        return itemRarityEnum;
      }
    }
    throw new IllegalArgumentException("Invalid item rarity");
  }
}
