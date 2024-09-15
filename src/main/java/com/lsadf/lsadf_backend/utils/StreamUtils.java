package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtils {
    public static Stream<UserEntity> sortUsers(Stream<UserEntity> userStream, UserOrderBy orderBy) {
        return switch (orderBy) {
            case ID -> userStream.sorted(Comparator.comparing(UserEntity::getId));
            case ID_DESC -> userStream.sorted(Comparator.comparing(UserEntity::getId).reversed());
            case EMAIL -> userStream.sorted(Comparator.comparing(UserEntity::getEmail));
            case EMAIL_DESC -> userStream.sorted(Comparator.comparing(UserEntity::getEmail).reversed());
            case NAME -> userStream.sorted(Comparator.comparing(UserEntity::getName));
            case NAME_DESC -> userStream.sorted(Comparator.comparing(UserEntity::getName).reversed());
            case CREATED_AT -> userStream.sorted(Comparator.comparing(UserEntity::getCreatedAt));
            case CREATED_AT_DESC -> userStream.sorted(Comparator.comparing(UserEntity::getCreatedAt).reversed());
            case UPDATED_AT -> userStream.sorted(Comparator.comparing(UserEntity::getUpdatedAt));
            case UPDATED_AT_DESC -> userStream.sorted(Comparator.comparing(UserEntity::getUpdatedAt).reversed());
            case PROVIDER -> userStream.sorted(Comparator.comparing(UserEntity::getProvider));
            case PROVIDER_DESC -> userStream.sorted(Comparator.comparing(UserEntity::getProvider).reversed());
            case NONE -> userStream;
        };
    }

    public static Stream<GameSaveEntity> sortGameSaves(Stream<GameSaveEntity> gameSaveStream, GameSaveOrderBy orderBy) {
        return switch (orderBy) {
            case CREATED_AT -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getCreatedAt));
            case CREATED_AT_DESC -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getCreatedAt).reversed());
            case UPDATED_AT -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getUpdatedAt));
            case UPDATED_AT_DESC -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getUpdatedAt).reversed());
            case ID -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getId));
            case ID_DESC -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getId).reversed());
            case GOLD -> gameSaveStream.sorted(Comparator.comparing(gameSaveEntity -> gameSaveEntity.getCurrencyEntity().getGoldAmount()));
            case GOLD_DESC -> gameSaveStream.sorted(Comparator.comparing(gameSaveEntity -> ((GameSaveEntity) gameSaveEntity).getCurrencyEntity().getGoldAmount()).reversed());
            case DIAMOND -> gameSaveStream.sorted(Comparator.comparing(gameSaveEntity -> gameSaveEntity.getCurrencyEntity().getDiamondAmount()));
            case DIAMOND_DESC -> gameSaveStream.sorted(Comparator.comparing(gameSaveEntity -> ((GameSaveEntity) gameSaveEntity).getCurrencyEntity().getDiamondAmount()).reversed());
            case EMERALD -> gameSaveStream.sorted(Comparator.comparing(gameSaveEntity -> gameSaveEntity.getCurrencyEntity().getEmeraldAmount()));
            case EMERALD_DESC -> gameSaveStream.sorted(Comparator.comparing(gameSaveEntity -> ((GameSaveEntity) gameSaveEntity).getCurrencyEntity().getEmeraldAmount()).reversed());
            case AMETHYST -> gameSaveStream.sorted(Comparator.comparing(gameSaveEntity -> gameSaveEntity.getCurrencyEntity().getAmethystAmount()));
            case AMETHYST_DESC -> gameSaveStream.sorted(Comparator.comparing(gameSaveEntity -> ((GameSaveEntity) gameSaveEntity).getCurrencyEntity().getAmethystAmount()).reversed());
            case NONE -> gameSaveStream;
        };
    }
}
