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

    // GAME SAVE
    public static class GameSave {
        public static final String GAME_SAVE_ENTITY = "t_game_save";
        public static final String GAME_SAVE_HEALTH_POINTS = "game_save_health_points";
        public static final String GAME_SAVE_ATTACK = "game_save_attack";
        public static final String GAME_SAVE_GOLD_ID = "game_save_gold_id";
    }

    // CURRENCIES
    public static class Currencies {
        public static final String CURRENCY_ENTITY = "t_currency";
        public static final String CURRENCY_GOLD_AMOUNT = "currency_gold_amount";
        public static final String CURRENCY_DIAMOND_AMOUNT = "currency_diamond_amount";
        public static final String CURRENCY_EMERALD_AMOUNT = "currency_emerald_amount";
        public static final String CURRENCY_AMETHYST_AMOUNT = "currency_amethyst_amount";
        public static final String CURRENCY_USER_EMAIL = "currency_user_email";

    }

    // USER
    public static class User {
        public static final String USER_ENTITY = "t_user";
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_EMAIL = "user_email";
        public static final String USER_PASSWORD = "user_password";
        public static final String USER_ROLES = "user_roles";
        public static final String USER_ENABLED = "user_enabled";
    }
}
