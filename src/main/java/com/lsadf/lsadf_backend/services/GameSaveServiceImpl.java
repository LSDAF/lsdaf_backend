package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.utils.GameSaveUtils;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Implementation of GameSaveService
 */
@Slf4j
public class GameSaveServiceImpl implements GameSaveService {
    private final UserRepository userRepository;
    private final GameSaveRepository gameSaveRepository;


    public GameSaveServiceImpl(UserRepository userRepository,
                               GameSaveRepository gameSaveRepository) {
        this.userRepository = userRepository;
        this.gameSaveRepository = gameSaveRepository;
    }

    @Override
    public GameSave createGameSave(String userEmail) throws NotFoundException {
        log.info("Creating new save for user {}", userEmail);
        UserEntity userEntity = userRepository.findUserEntityByEmail(userEmail).orElseThrow(NotFoundException::new);

        GameSaveEntity gameSaveEntity = GameSaveEntity
                .builder()
                .user(userEntity)
                .build();

        GameSaveEntity newGameSaveEntity = gameSaveRepository.save(gameSaveEntity);
        User user = UserUtils.createUserFromEntity(userEntity);

        return GameSaveUtils.createGameSaveFromEntity(newGameSaveEntity, user);
    }

    @Override
    public GameSave getGameSave(String saveId) throws ForbiddenException, NotFoundException {
        return getGameSave(saveId, null);
    }

    @Override
    public GameSave getGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException {
        log.info("Getting save by id: {}", saveId);
        GameSaveEntity gameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);

        validateGameSaveOwnership(gameSave, userEmail);

        return GameSaveUtils.createGameSaveFromEntity(gameSave, UserUtils.createUserFromEntity(gameSave.getUser()));
    }

    @Override
    public GameSave updateGameSave(String saveId, GameSave newSaveGame, String userEmail) throws ForbiddenException, NotFoundException {

        User currentUser = newSaveGame.getUser();

        // Assert userEmail is the same as the game user id
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);

        validateGameSaveOwnership(currentGameSave, userEmail);

        log.info("Updating game with id {}", saveId);

        GameSaveEntity updatedGameSaveEntity = updateGameSaveEntity(currentGameSave, newSaveGame);

        return GameSaveUtils.createGameSaveFromEntity(updatedGameSaveEntity, currentUser);
    }

    @Override
    public void deleteGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException {

        // Assert userEmail is the same as the game user id
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);

        validateGameSaveOwnership(currentGameSave, userEmail);

        log.info("Deleting game with id {}", saveId);

        gameSaveRepository.deleteById(saveId);
    }

    private GameSaveEntity updateGameSaveEntity(GameSaveEntity gameSaveEntity, GameSave newGameSave) {
        boolean hasUpdates = false;

        if (gameSaveEntity.getAttack() != newGameSave.getAttack()) {
            gameSaveEntity.setAttack(newGameSave.getAttack());
            hasUpdates = true;
        }
        if (gameSaveEntity.getHealthPoints() != newGameSave.getHealthPoints()) {
            gameSaveEntity.setHealthPoints(newGameSave.getHealthPoints());
            hasUpdates = true;
        }
        if (gameSaveEntity.getGold() != newGameSave.getGold()) {
            gameSaveEntity.setGold(newGameSave.getGold());
            hasUpdates = true;
        }

        if (hasUpdates) {
            return gameSaveRepository.save(gameSaveEntity);
        }

        return gameSaveEntity;
    }

    private static void validateGameSaveOwnership(GameSaveEntity gameSaveEntity, String userEmail) throws ForbiddenException {
        if (!Objects.equals(gameSaveEntity.getUser().getEmail(), userEmail)) {
            throw new ForbiddenException("Given user with email " + userEmail + " is not the owner of the game with id " + gameSaveEntity.getId() + ".");
        }
    }
}
