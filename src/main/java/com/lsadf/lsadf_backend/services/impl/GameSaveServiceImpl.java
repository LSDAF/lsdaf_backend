package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.properties.CacheProperties;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.GoldRepository;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementation of GameSaveService
 */
@Slf4j
public class GameSaveServiceImpl implements GameSaveService {
    private final UserService userService;
    private final GameSaveRepository gameSaveRepository;
    private final GoldRepository goldRepository;
    private final CacheService cacheService;

    private final Mapper mapper;

    private final boolean cacheEnabled;


    public GameSaveServiceImpl(UserService userService, GameSaveRepository gameSaveRepository, GoldRepository goldRepository, CacheService cacheService, Mapper mapper, CacheProperties cacheProperties) {
        this.userService = userService;
        this.gameSaveRepository = gameSaveRepository;
        this.goldRepository = goldRepository;
        this.cacheService = cacheService;
        this.mapper = mapper;
        this.cacheEnabled = cacheProperties.isEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSaveEntity createGameSave(String userEmail) throws NotFoundException {
        log.info("Creating new save for user {}", userEmail);

        UserEntity userEntity = userService.getUserByEmail(userEmail);

        GameSaveEntity entity = GameSaveEntity.builder()
                .user(userEntity)
                .build();

        var saved = gameSaveRepository.save(entity);

        GoldEntity goldEntity = GoldEntity.builder()
                .userEmail(userEntity.getEmail())
                .id(saved.getId())
                .build();

        saved.setGoldEntity(goldEntity);


        return gameSaveRepository.save(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GameSaveEntity getGameSave(String saveId) throws NotFoundException {
        return gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSaveEntity createGameSave(AdminGameSaveCreationRequest creationRequest) throws NotFoundException {

        UserEntity userEntity = userService.getUserByEmail(creationRequest.getUserEmail());

        GameSaveEntity entity = GameSaveEntity.builder()
                .healthPoints(creationRequest.getHealthPoints())
                .attack(creationRequest.getAttack())
                .user(userEntity)
                .build();

        var saved = gameSaveRepository.save(entity);

        GoldEntity goldEntity = GoldEntity.builder()
                .userEmail(userEntity.getEmail())
                .goldAmount(creationRequest.getGold())
                .build();

        saved.setGoldEntity(goldEntity);

        if (creationRequest.getId() != null) {
            if (gameSaveRepository.existsById(creationRequest.getId())) {
                throw new AlreadyExistingGameSaveException("Game save with id " + creationRequest.getId() + " already exists");
            }
            entity.setId(creationRequest.getId());
        }

        return gameSaveRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSaveEntity updateGameSave(String saveId, GameSaveUpdateRequest updateRequest) throws NotFoundException {
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);
        return updateGameSaveEntity(currentGameSave, updateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSaveEntity updateGameSave(String saveId, AdminGameSaveUpdateRequest updateRequest) throws NotFoundException {
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);
        return updateGameSaveEntity(currentGameSave, updateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteGameSave(String saveId) throws NotFoundException {
        if (saveId == null) {
            throw new NotFoundException("Game save id is null");
        }
        if (!gameSaveRepository.existsById(saveId)) {
            log.error("Game save with id {} not found", saveId);
            throw new NotFoundException("Game save with id " + saveId + " not found");
        }
        gameSaveRepository.deleteById(saveId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stream<GameSaveEntity> getGameSaves() {
        return gameSaveRepository.findAllGameSaves();
    }

    private GameSaveEntity updateGameSaveEntity(GameSaveEntity gameSaveEntity, GameSaveUpdateRequest updateRequest) {
        boolean hasUpdates = false;

        if (gameSaveEntity.getAttack() != updateRequest.getAttack()) {
            gameSaveEntity.setAttack(updateRequest.getAttack());
            hasUpdates = true;
        }
        if (gameSaveEntity.getHealthPoints() != updateRequest.getHealthPoints()) {
            gameSaveEntity.setHealthPoints(updateRequest.getHealthPoints());
            hasUpdates = true;
        }

        if (hasUpdates) {
            return gameSaveRepository.save(gameSaveEntity);
        }

        return gameSaveEntity;
    }

    private GameSaveEntity updateGameSaveEntity(GameSaveEntity gameSaveEntity, AdminGameSaveUpdateRequest adminUpdateRequest) {
        boolean hasUpdates = false;

        if (gameSaveEntity.getAttack() != adminUpdateRequest.getAttack()) {
            gameSaveEntity.setAttack(adminUpdateRequest.getAttack());
            hasUpdates = true;
        }
        if (gameSaveEntity.getHealthPoints() != adminUpdateRequest.getHealthPoints()) {
            gameSaveEntity.setHealthPoints(adminUpdateRequest.getHealthPoints());
            hasUpdates = true;
        }

        GoldEntity goldEntity = gameSaveEntity.getGoldEntity();

        if (goldEntity.getGoldAmount() != adminUpdateRequest.getGold()) {
            goldEntity.setGoldAmount(adminUpdateRequest.getGold());
            hasUpdates = true;
        }

        if (hasUpdates) {
            return gameSaveRepository.save(gameSaveEntity);
        }

        return gameSaveEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void checkGameSaveOwnership(String saveId, String userEmail) throws ForbiddenException, NotFoundException {
        if (!cacheEnabled) {
            GameSaveEntity gameSaveEntity = getGameSave(saveId);

            if (!Objects.equals(gameSaveEntity.getUser().getEmail(), userEmail)) {
                throw new ForbiddenException("The given user email is not the owner of the game save");
            }

            return;
        }

        Optional<String> optionalOwnership = cacheService.getGameSaveOwnership(saveId);
        if (optionalOwnership.isEmpty()) {
            GameSaveEntity gameSaveEntity = getGameSave(saveId);
            cacheService.setGameSaveOwnership(saveId, userEmail);
            if (!Objects.equals(gameSaveEntity.getUser().getEmail(), userEmail)) {
                throw new ForbiddenException("The given user email is not the owner of the game save");
            }
            return;
        }

        if (!Objects.equals(optionalOwnership.get(), userEmail)) {
            throw new ForbiddenException("The given user email is not the owner of the game save");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameSaveEntity> getGameSavesByUserEmail(String userEmail) {
        return gameSaveRepository.findGameSaveEntitiesByUserEmail(userEmail);
    }
}
