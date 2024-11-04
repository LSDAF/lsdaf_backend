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

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GenericResponse {
        public static final String STATUS = "status";
        public static final String MESSAGE = "message";
        public static final String DATA = "data";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GameSave {
        public static final String HP = "health_points";
        public static final String USER_EMAIL = "user_email";
        public static final String ATTACK = "attack";
        public static final String NICKNAME = "nickname";
        public static final String CURRENCY = "currency";
        public static final String STAGE = "stage";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class User {
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String USERNAME = "username";
        public static final String CREATED_TIMESTAMP = "created_timestamp";
        public static final String PASSWORD = "password";
        public static final String ENABLED = "enabled";
        public static final String EMAIL_VERIFIED = "email_verified";
        public static final String USER_ROLES = "user_roles";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SearchRequest {
        public static final String FILTERS = "filters";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserInfo {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String ROLES = "roles";
        public static final String VERIFIED = "validated";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Stage {
        public static final String CURRENT_STAGE = "current_stage";
        public static final String MAX_STAGE = "max_stage";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Currency {
        public static final String GOLD = "gold";
        public static final String DIAMOND = "diamond";
        public static final String EMERALD = "emerald";
        public static final String AMETHYST = "amethyst";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class JwtAuthentication {
        public static final String ACCESS_TOKEN = "access_token";
        public static final String REFRESH_TOKEN = "refresh_token";
        public static final String EXPIRES_IN = "expires_in";
        public static final String REFRESH_EXPIRES_IN = "refresh_expires_in";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Filter {
        public static final String TYPE = "type";
        public static final String VALUE = "value";
    }
}
