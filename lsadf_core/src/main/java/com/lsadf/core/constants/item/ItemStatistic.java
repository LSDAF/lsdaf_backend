package com.lsadf.core.constants.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatistic {
  ATTACK_ADD("attack_add"),
  ATTACK_MULT("attack_mult"),
  CRIT_CHANCE("crit_chance"),
  CRIT_DAMAGE("crit_damage"),
  HEALTH_ADD("health_add"),
  HEALTH_MULT("health_mult"),
  RESISTANCE_ADD("resistance_add"),
  RESISTANCE_MULT("resistance_mult");

  private final String statistic;

  public static ItemStatistic fromString(String statistic) {
    for (ItemStatistic itemStatistic : ItemStatistic.values()) {
      if (itemStatistic.getStatistic().equalsIgnoreCase(statistic)) {
        return itemStatistic;
      }
    }
    throw new IllegalArgumentException("Invalid item statistic");
  }
}
