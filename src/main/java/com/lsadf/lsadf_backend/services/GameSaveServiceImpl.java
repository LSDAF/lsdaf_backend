package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Implementation of GameSaveService
 */
@Slf4j
@RequiredArgsConstructor
public class GameSaveServiceImpl implements GameSaveService {
    private final UserRepository userRepository;
    private final GameSaveRepository gameSaveRepository;
    private final Mapper mapper;


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSave createGameSave(String userEmail) throws NotFoundException {
        log.info("Creating new save for user {}", userEmail);
        UserEntity userEntity = userRepository.findUserEntityByEmail(userEmail).orElseThrow(NotFoundException::new);

        GameSaveEntity gameSaveEntity = GameSaveEntity
                .builder()
                .user(userEntity)
                .build();

        GameSaveEntity newGameSaveEntity = gameSaveRepository.save(gameSaveEntity);

        return mapper.mapToGameSave(newGameSaveEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GameSave getGameSave(String saveId) throws ForbiddenException, NotFoundException {
        return getGameSave(saveId, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GameSave getGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException {
        log.info("Getting save by id: {}", saveId);
        GameSaveEntity gameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);

        validateGameSaveOwnership(gameSave, userEmail);

        return mapper.mapToGameSave(gameSave);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSave updateGameSave(String saveId, GameSave newSaveGame, String userEmail) throws ForbiddenException, NotFoundException {

        // Assert userEmail is the same as the game user id
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);

        validateGameSaveOwnership(currentGameSave, userEmail);

        log.info("Updating game with id {}", saveId);

        GameSaveEntity updatedGameSaveEntity = updateGameSaveEntity(currentGameSave, newSaveGame);

        return mapper.mapToGameSave(updatedGameSaveEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
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
