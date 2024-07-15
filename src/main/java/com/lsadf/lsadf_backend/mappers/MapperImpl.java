package com.lsadf.lsadf_backend.mappers;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MapperImpl implements Mapper {
    @Override
    public GameSave mapToGameSave(GameSaveEntity gameSaveEntity) {
        return GameSave.builder()
                .userId(gameSaveEntity.getUser().getId())
                .gold(gameSaveEntity.getGold())
                .healthPoints(gameSaveEntity.getHealthPoints())
                .attack(gameSaveEntity.getAttack())
                .id(gameSaveEntity.getId())
                .createdAt(gameSaveEntity.getCreatedAt())
                .updatedAt(gameSaveEntity.getUpdatedAt())
                .build();
    }

    @Override
    public User mapToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .email(userEntity.getEmail())
                .build();
    }
}
