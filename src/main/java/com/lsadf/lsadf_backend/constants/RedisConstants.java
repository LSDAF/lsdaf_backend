package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Redis constants class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisConstants {
    public static final String GOLD = "gold:";
    public static final String GOLD_HISTO = "gold_histo:";
    public static final String GAME_SAVE_OWNERSHIP = "gamesaveownership:";
    public static final String LOCAL_USER = "local_user:";
}
