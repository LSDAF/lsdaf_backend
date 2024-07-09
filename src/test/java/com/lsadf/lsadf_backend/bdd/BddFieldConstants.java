package com.lsadf.lsadf_backend.bdd;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BddFieldConstants {
    public static final class GameSave {
        public static final String ID = "id";
        public static final String USER_ID = "userId";
        public static final String GOLD = "gold";
        public static final String HEALTH_POINTS = "healthPoints";
        public static final String ATTACK = "attack";
    }

    public static final class User {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
    }
}
