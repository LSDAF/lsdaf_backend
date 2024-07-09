package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.entity.GameSaveEntity;
import com.lsadf.lsadf_backend.models.entity.UserEntity;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Utility class for BDD tests
 */
@UtilityClass
public class BddUtils {

    /**
     * Maps a row from a BDD table to a GameSaveEntity
     * @param row
     * @param userRepository
     * @return
     */
    public static GameSaveEntity mapToGameSaveEntity(Map<String, String> row, UserRepository userRepository) {
        String id = row.get(BddFieldConstants.GameSave.ID);
        String userId = row.get(BddFieldConstants.GameSave.USER_ID);
        long gold = Long.parseLong(row.get(BddFieldConstants.GameSave.GOLD));
        long healthPoints = Long.parseLong(row.get(BddFieldConstants.GameSave.HEALTH_POINTS));
        long attack = Long.parseLong(row.get(BddFieldConstants.GameSave.ATTACK));

        UserEntity user = userRepository.findById(userId).orElseThrow();

        GameSaveEntity gameSaveEntity =  GameSaveEntity.builder()
                .user(user)
                .attack(attack)
                .healthPoints(healthPoints)
                .gold(gold)
                .build();

        gameSaveEntity.setId(id);

        return gameSaveEntity;
    }

    /**
     * Maps a row from a BDD table to a GameSave
     * @param row
     * @param userRepository
     * @return
     */
    public static GameSave mapToGameSave(Map<String, String> row, UserRepository userRepository) {
        String userId = row.get(BddFieldConstants.GameSave.USER_ID);
        long gold = Long.parseLong(row.get(BddFieldConstants.GameSave.GOLD));
        long healthPoints = Long.parseLong(row.get(BddFieldConstants.GameSave.HEALTH_POINTS));
        long attack = Long.parseLong(row.get(BddFieldConstants.GameSave.ATTACK));

        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        User user = UserUtils.createUserFromEntity(userEntity);

        return GameSave.builder()
                .attack(attack)
                .user(user)
                .healthPoints(healthPoints)
                .gold(gold)
                .build();
    }

    /**
     * Maps a row from a BDD table to a UserEntity
     * @param row
     * @return
     */
    public static UserEntity mapToUserEntity(Map<String, String> row) {
        String email = row.get(BddFieldConstants.User.EMAIL);
        String name = row.get(BddFieldConstants.User.NAME);
        String id = row.get(BddFieldConstants.User.ID);


        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .name(name)
                .build();

        userEntity.setId(id);

        return userEntity;
    }
}
