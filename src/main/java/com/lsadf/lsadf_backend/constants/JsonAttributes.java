package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonAttributes {
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    public static final class GameSave {
        public static final String HP = "hp";
        public static final String GOLD = "gold";
        public static final String ATTACK = "atk";
    }

    public static final class User {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
    }
}
