package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtils {
    public static Stream<User> sortUsers(Stream<User> userStream, UserOrderBy orderBy) {
        orderBy = orderBy == null ? UserOrderBy.NONE : orderBy;
        return switch (orderBy) {
            case ID -> userStream.sorted(Comparator.comparing(User::getId));
            case ID_DESC -> userStream.sorted(Comparator.comparing(User::getId).reversed());
            case EMAIL -> userStream.sorted(Comparator.comparing(User::getUsername));
            case EMAIL_DESC -> userStream.sorted(Comparator.comparing(User::getUsername).reversed());
            case FIRST_NAME -> userStream.sorted(Comparator.comparing(User::getLastName));
            case FIRST_NAME_DESC -> userStream.sorted(Comparator.comparing(User::getLastName).reversed());
            case LAST_NAME -> userStream.sorted(Comparator.comparing(User::getLastName));
            case LAST_NAME_DESC -> userStream.sorted(Comparator.comparing(User::getLastName).reversed());
            case CREATED_AT -> userStream.sorted(Comparator.comparing(User::getCreatedTimestamp));
            case CREATED_AT_DESC -> userStream.sorted(Comparator.comparing(User::getCreatedTimestamp).reversed());
            case NONE -> userStream;
        };
    }

    public static Stream<GameSaveEntity> sortGameSaves(Stream<GameSaveEntity> gameSaveStream, GameSaveOrderBy orderBy) {
        orderBy = orderBy == null ? GameSaveOrderBy.NONE : orderBy;
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
