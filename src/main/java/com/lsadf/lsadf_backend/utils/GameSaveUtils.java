package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.entity.GameSaveEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameSaveUtils {
    public static GameSave createGameSaveFromEntity(GameSaveEntity gameSaveEntity, User user) {
        return GameSave.builder()
                .user(user)
                .gold(gameSaveEntity.getGold())
                .healthPoints(gameSaveEntity.getHealthPoints())
                .attack(gameSaveEntity.getAttack())
                .id(gameSaveEntity.getId())
                .createdAt(gameSaveEntity.getCreatedAt())
                .updatedAt(gameSaveEntity.getUpdatedAt())
                .build();
    }
}
