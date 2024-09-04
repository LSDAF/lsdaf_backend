package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constant class containing all property names for JSON transfer objects
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonAttributes {
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    public static final class GameSave {
        public static final String HP = "health_points";
        public static final String USER_ID = "user_id";
        public static final String USER_EMAIL = "user_email";
        public static final String GOLD = "gold";
        public static final String ATTACK = "attack";
    }

    public static final class User {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String ENABLED = "enabled";
        public static final String PROVIDER = "provider";
        public static final String USER_ROLES = "user_roles";
    }

    public static final class UserAdminDetails {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String ENABLED = "enabled";
        public static final String PROVIDER = "provider";
        public static final String USER_ROLES = "user_roles";
        public static final String GAME_SAVES = "game_saves";
    }

    public static final class UserInfo {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String ROLES = "roles";
    }

    public static final class Gold {
        public static final String AMOUNT = "amount";
    }

    public static final class JwtAuthentication {
        public static final String ACCESS_TOKEN = "access_token";
        public static final String USER_INFO = "user_info";
    }
}
