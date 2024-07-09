package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Controller constants class
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerConstants {

    public static final String GAME_SAVE = "game_save";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Swagger {
        public static final String GAME_SAVE_CONTROLLER = "Game Save Controller";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class GameSave {
        public static final String GENERATE = "/generate";
        public static final String GAME_SAVE_ID = "/{game_save_id}";
    }
}
