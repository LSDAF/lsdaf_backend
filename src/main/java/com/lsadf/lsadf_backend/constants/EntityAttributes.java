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
        public static final String GAME_SAVE_ID = "game_save_id";
        public static final String GAME_SAVE_USER_ID = "game_save_user_id";
        public static final String GAME_SAVE_BANK_ACCOUNT_ID = "game_save_game_state";
    }

    // USER
    public static class User {
        public static final String USER_ENTITY = "t_user";
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_EMAIL = "user_email";
        public static final String USER_PASSWORD = "user_password";
    }
}
