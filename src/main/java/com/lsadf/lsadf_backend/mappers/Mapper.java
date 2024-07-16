package com.lsadf.lsadf_backend.mappers;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.User;

public interface Mapper {
    /**
     * Maps GameSaveEntity to GameSave
     * @param gameSaveEntity GameSaveEntity
     * @return
     */
    GameSave mapToGameSave(GameSaveEntity gameSaveEntity);
    User mapToUser(UserEntity userEntity);
}
