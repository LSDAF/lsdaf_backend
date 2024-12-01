package com.lsadf.core.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Redis constants class */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisConstants {
  public static final String CHARACTERISTICS_HISTO = "characteristics_histo:";
  public static final String CHARACTERISTICS = "characteristics:";
  public static final String CURRENCY = "currency:";
  public static final String CURRENCY_HISTO = "currency_histo:";
  public static final String INVENTORY = "inventory:";
  public static final String INVENTORY_HISTO = "inventory_histo:";
  public static final String STAGE = "stage:";
  public static final String STAGE_HISTO = "stage_histo:";
  public static final String GAME_SAVE_OWNERSHIP = "game_save_ownership:";
  public static final String INVALIDATED_JWT_TOKEN = "invalidated_jwt_token:";
}
