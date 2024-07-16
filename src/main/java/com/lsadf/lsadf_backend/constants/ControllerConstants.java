package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Controller constants class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerConstants {

    public static final String GAME_SAVE = "/api/v1/game_save";
    public static final String AUTH = "/api/v1/auth";
    public static final String USER = "/api/v1/user";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Swagger {
        public static final String GAME_SAVE_CONTROLLER = "Game Save Controller";
        public static final String USER_CONTROLLER = "User Controller";
        public static final String AUTH_CONTROLLER = "Auth Controller";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GameSave {
        public static final String GENERATE = "/generate";
        public static final String GAME_SAVE_ID = "/{game_save_id}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Auth {
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class User {
        public static final String INFO = "/info";
    }
}
