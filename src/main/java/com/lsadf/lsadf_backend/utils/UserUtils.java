package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtils {
    public static User createUserFromEntity(UserEntity userEntity) {

        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

}
