package com.lsadf.lsadf_backend.bdd;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BddFieldConstants {

    private static final String ID = "id";
    private static final String CREATED_AT = "createdAt";
    private static final String UPDATED_AT = "updatedAt";

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

    public static final class UserCreationRequest {
        public static final String NAME = "name";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
    }

    public static final class UserInfo {
        public static final String ID = BddFieldConstants.ID;
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String ROLES = "roles";
    }

    public static final class UserLoginRequest {
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

    public static final class JwtAuthentication {
        public static final String NAME = "name";
        public static final String ID = "id";
        public static final String ACCESS_TOKEN = "accessToken";
        public static final String EMAIL = "email";
        public static final String ROLES = "roles";
    }
}
