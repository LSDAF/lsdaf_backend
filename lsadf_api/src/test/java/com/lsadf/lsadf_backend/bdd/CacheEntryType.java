package com.lsadf.lsadf_backend.bdd;

public enum CacheEntryType {
    CURRENCY,
    CURRENCY_HISTO,
    STAGE,
    STAGE_HISTO,
    GAME_SAVE_OWNERSHIP;

    public static CacheEntryType fromString(String cacheEntryType) {
        for (CacheEntryType value : CacheEntryType.values()) {
            if (value.name().equalsIgnoreCase(cacheEntryType)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid cache entry type: " + cacheEntryType);
    }
}
