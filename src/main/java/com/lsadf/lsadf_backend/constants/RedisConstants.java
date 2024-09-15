package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Redis constants class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisConstants {
    public static final String CURRENCY = "currency:";
    public static final String CURRENCY_HISTO = "currency_histo:";
    public static final String GAME_SAVE_OWNERSHIP = "gamesaveownership:";
}
