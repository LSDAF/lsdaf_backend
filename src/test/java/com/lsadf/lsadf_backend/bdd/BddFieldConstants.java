package com.lsadf.lsadf_backend.bdd;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;

/**
 * Constants for the fields in the BDD scenarios
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BddFieldConstants {

    private static final String ID = "id";
    private static final String CREATED_AT = "createdAt";
    private static final String UPDATED_AT = "updatedAt";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GoldCacheEntry {
        public static final String GAME_SAVE_ID = "gameSaveId";
        public static final String GOLD = "gold";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Gold {
        public static final String ID = "id";
        public static final String AMOUNT = "amount";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GameSaveOwnershipCacheEntry {
        public static final String GAME_SAVE_ID = "gameSaveId";
        public static final String USER_ID = "userId";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GameSave {
        public static final String ID = BddFieldConstants.ID;
        public static final String USER_ID = "userId";
        public static final String USER_EMAIL = "userEmail";
        public static final String GOLD = "gold";
        public static final String HEALTH_POINTS = "healthPoints";
        public static final String ATTACK = "attack";
        public static final String CREATED_AT = BddFieldConstants.CREATED_AT;
        public static final String UPDATED_AT = BddFieldConstants.UPDATED_AT;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class User {
        public static final String ID = BddFieldConstants.ID;
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String ENABLED = "enabled";
        public static final String PROVIDER = "provider";
        public static final String ROLES = "roles";
        public static final String CREATED_AT = BddFieldConstants.CREATED_AT;
        public static final String UPDATED_AT = BddFieldConstants.UPDATED_AT;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserCreationRequest {
        public static final String NAME = "name";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserInfo {
        public static final String ID = BddFieldConstants.ID;
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String ROLES = "roles";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserLoginRequest {
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserUpdateRequest {
        public static final String NAME = "name";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class JwtAuthentication {
        public static final String NAME = "name";
        public static final String ID = "id";
        public static final String ACCESS_TOKEN = "accessToken";
        public static final String EMAIL = "email";
        public static final String ROLES = "roles";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class UserAdminDetails {
        public static final String ID = BddFieldConstants.ID;
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String NAME = "name";
        public static final String SOCIAL_PROVIDER = "socialProvider";
        public static final String ENABLED = "enabled";
        public static final String ROLES = "roles";
        public static final String GAME_SAVES = "gameSaves";
        public static final String UPDATED_AT = BddFieldConstants.UPDATED_AT;
        public static final String CREATED_AT = BddFieldConstants.CREATED_AT;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GlobalInfo {
        public static final String GAME_SAVE_COUNTER = "gameSaveCounter";
        public static final String USER_COUNTER = "userCounter";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AdminUserUpdateRequest {
        public static final String NAME = "name";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
        public static final String ENABLED = "enabled";
        public static final String USER_ROLES = "userRoles";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AdminUserCreationRequest {
        public static final String NAME = "name";
        public static final String USER_ID = "userId";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
        public static final String ENABLED = "enabled";
        public static final String SOCIAL_PROVIDER = "socialProvider";
        public static final String PROVIDER_USER_ID = "providerUserId";
        public static final String ROLES = "roles";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class SearchRequest {
        public static final String KEY = "key";
        public static final String VALUE = "value";
    }
}
