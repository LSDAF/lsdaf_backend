package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constant class containing all property names for database entities
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityAttributes {

    // COMMON
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    // REFRESH TOKEN
    public static class RefreshToken {
        public static final String REFRESH_TOKEN_ENTITY = "t_refresh_token";
        public static final String REFRESH_TOKEN_TOKEN = "token";
        public static final String REFRESH_TOKEN_STATUS = "status";
        public static final String REFRESH_TOKEN_EXPIRATION_DATE = "expiration_date";
        public static final String REFRESH_TOKEN_INVALIDATION_DATE = "invalidation_date";
    }

    // GAME SAVE
    public static class GameSave {
        public static final String GAME_SAVE_ENTITY = "t_game_save";
        public static final String GAME_SAVE_HEALTH_POINTS = "health_points";
        public static final String GAME_SAVE_ATTACK = "attack";
        public static final String GAME_SAVE_CURRENCY_ID = "currency_id";
    }

    // CURRENCIES
    public static class Currencies {
        public static final String CURRENCY_ENTITY = "t_currency";
        public static final String CURRENCY_GOLD_AMOUNT = "gold_amount";
        public static final String CURRENCY_DIAMOND_AMOUNT = "diamond_amount";
        public static final String CURRENCY_EMERALD_AMOUNT = "emerald_amount";
        public static final String CURRENCY_AMETHYST_AMOUNT = "amethyst_amount";
        public static final String CURRENCY_USER_EMAIL = "user_email";

    }

    // USER
    public static class User {
        public static final String USER_ENTITY = "t_user";
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "name";
        public static final String USER_EMAIL = "email";
        public static final String USER_PASSWORD = "password";
        public static final String USER_ROLES = "roles";
        public static final String USER_ENABLED = "enabled";
    }
}
