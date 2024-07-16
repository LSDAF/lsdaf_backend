package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.requests.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.UserOrderBy;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtils {
    public static Stream<UserEntity> sortUsers(Stream<UserEntity> userStream, Optional<UserOrderBy> orderBy) {
        if (orderBy.isEmpty()) {
            return userStream;
        }
        UserOrderBy userOrderBy = orderBy.get();
        return switch (userOrderBy) {
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
        };
    }

    public static Stream<GameSaveEntity> sortGameSaves(Stream<GameSaveEntity> gameSaveStream, Optional<GameSaveOrderBy> orderBy) {
        if (orderBy.isEmpty()) {
            return gameSaveStream;
        }
        GameSaveOrderBy gameSaveOrderBy = orderBy.get();
        return switch (gameSaveOrderBy) {
            case CREATED_AT -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getCreatedAt));
            case CREATED_AT_DESC -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getCreatedAt).reversed());
            case UPDATED_AT -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getUpdatedAt));
            case UPDATED_AT_DESC -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getUpdatedAt).reversed());
            case ID -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getId));
            case ID_DESC -> gameSaveStream.sorted(Comparator.comparing(GameSaveEntity::getId).reversed());
        };
    }
}
