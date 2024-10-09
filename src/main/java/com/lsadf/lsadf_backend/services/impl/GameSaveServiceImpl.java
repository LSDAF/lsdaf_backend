package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.lsadf_backend.exceptions.AlreadyTakenNicknameException;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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

    private final Cache<String> gameSaveOwnershipCache;
    private final HistoCache<Stage> stageCache;
    private final HistoCache<Currency> currencyCache;


    public GameSaveServiceImpl(UserService userService,
                               GameSaveRepository gameSaveRepository,
                               Cache<String> gameSaveOwnershipCache,
                               HistoCache<Stage> stageCache,
                               HistoCache<Currency> currencyCache) {
        this.userService = userService;
        this.gameSaveRepository = gameSaveRepository;
        this.gameSaveOwnershipCache = gameSaveOwnershipCache;
        this.stageCache = stageCache;
        this.currencyCache = currencyCache;
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

        GameSaveEntity saved = gameSaveRepository.save(entity);
        saved.setNickname(saved.getId());

        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .userEmail(userEntity.getEmail())
                .id(saved.getId())
                .build();

        saved.setCurrencyEntity(currencyEntity);

        StageEntity stageEntity = StageEntity.builder()
                .userEmail(userEntity.getEmail())
                .id(saved.getId())
                .build();

        saved.setStageEntity(stageEntity);

        return gameSaveRepository.save(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GameSaveEntity getGameSave(String saveId) throws NotFoundException {
        GameSaveEntity gameSaveEntity = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);
        return enrichGameSaveWithCachedData(gameSaveEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSaveEntity createGameSave(AdminGameSaveCreationRequest creationRequest) throws NotFoundException, AlreadyExistingGameSaveException {

        UserEntity userEntity = userService.getUserByEmail(creationRequest.getUserEmail());

        GameSaveEntity entity = GameSaveEntity.builder()
                .healthPoints(creationRequest.getHealthPoints())
                .attack(creationRequest.getAttack())
                .user(userEntity)
                .build();

        if (creationRequest.getId() != null) {
            if (gameSaveRepository.existsById(creationRequest.getId())) {
                throw new AlreadyExistingGameSaveException("Game save with id " + creationRequest.getId() + " already exists");
            }
            entity.setId(creationRequest.getId());
        }

        GameSaveEntity saved = gameSaveRepository.save(entity);

        saved.setNickname(entity.getId());

        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .userEmail(userEntity.getEmail())
                .id(saved.getId())
                .goldAmount(creationRequest.getGold())
                .diamondAmount(creationRequest.getDiamond())
                .emeraldAmount(creationRequest.getEmerald())
                .amethystAmount(creationRequest.getAmethyst())
                .build();

        saved.setCurrencyEntity(currencyEntity);

        StageEntity stageEntity = StageEntity.builder()
                .currentStage(creationRequest.getCurrentStage())
                .maxStage(creationRequest.getMaxStage())
                .userEmail(userEntity.getEmail())
                .id(saved.getId())
                .build();

        saved.setStageEntity(stageEntity);

        return gameSaveRepository.save(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSaveEntity updateNickname(String saveId, GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest) throws NotFoundException, AlreadyTakenNicknameException {
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);

        if (gameSaveRepository.findGameSaveEntityByNickname(gameSaveUpdateNicknameRequest.getNickname()).isPresent()) {
            throw new AlreadyTakenNicknameException("Game save with nickname " + gameSaveUpdateNicknameRequest.getNickname() + " already exists");
        }

        return updateGameSaveEntityNickname(currentGameSave, gameSaveUpdateNicknameRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public GameSaveEntity updateNickname(String saveId, AdminGameSaveUpdateRequest updateRequest) throws NotFoundException, AlreadyTakenNicknameException {
        GameSaveEntity currentGameSave = gameSaveRepository.findById(saveId)
                .orElseThrow(NotFoundException::new);

        if (gameSaveRepository.findGameSaveEntityByNickname(updateRequest.getNickname()).isPresent()) {
            throw new AlreadyTakenNicknameException("Game save with nickname " + updateRequest.getNickname() + " already exists");
        }

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
        if (currencyCache.isEnabled()) {
            currencyCache.unset(saveId);
        }
        if (stageCache.isEnabled()) {
            stageCache.unset(saveId);
        }
        if (gameSaveOwnershipCache.isEnabled()) {
            gameSaveOwnershipCache.unset(saveId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stream<GameSaveEntity> getGameSaves() {
        return gameSaveRepository.findAllGameSaves()
                .map(this::enrichGameSaveWithCachedData);
    }

    /**
     * Update the game save entity with the new nickname
     *
     * @param gameSaveEntity                the game save entity
     * @param gameSaveUpdateNicknameRequest the nickname update request
     * @return the updated game save entity
     */
    private GameSaveEntity updateGameSaveEntityNickname(GameSaveEntity gameSaveEntity, GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest) {
        boolean hasUpdates = false;

        if (!Objects.equals(gameSaveEntity.getNickname(), gameSaveUpdateNicknameRequest.getNickname())) {
            gameSaveEntity.setNickname(gameSaveUpdateNicknameRequest.getNickname());
            hasUpdates = true;
        }

        if (hasUpdates) {
            return gameSaveRepository.save(gameSaveEntity);
        }

        return gameSaveEntity;
    }

    /**
     * Update the game save entity with the new data
     *
     * @param gameSaveEntity     the game save entity
     * @param adminUpdateRequest the admin update request
     * @return the updated game save entity
     */
    private GameSaveEntity updateGameSaveEntity(GameSaveEntity gameSaveEntity, AdminGameSaveUpdateRequest adminUpdateRequest) {
        boolean hasUpdates = false;

        if (!Objects.equals(gameSaveEntity.getNickname(), adminUpdateRequest.getNickname())) {
            gameSaveEntity.setNickname(adminUpdateRequest.getNickname());
            hasUpdates = true;
        }

        if (gameSaveEntity.getAttack() != adminUpdateRequest.getAttack()) {
            gameSaveEntity.setAttack(adminUpdateRequest.getAttack());
            hasUpdates = true;
        }
        if (gameSaveEntity.getHealthPoints() != adminUpdateRequest.getHealthPoints()) {
            gameSaveEntity.setHealthPoints(adminUpdateRequest.getHealthPoints());
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
        if (!gameSaveOwnershipCache.isEnabled()) {
            GameSaveEntity gameSaveEntity = this.gameSaveRepository.findById(saveId)
                    .orElseThrow(NotFoundException::new);

            if (!Objects.equals(gameSaveEntity.getUser().getEmail(), userEmail)) {
                throw new ForbiddenException("The given user email is not the owner of the game save");
            }

            return;
        }

        Optional<String> optionalOwnership = gameSaveOwnershipCache.get(saveId);
        if (optionalOwnership.isEmpty()) {
            GameSaveEntity gameSaveEntity = this.gameSaveRepository.findById(saveId)
                    .orElseThrow(NotFoundException::new);
            gameSaveOwnershipCache.set(saveId, userEmail);
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
    public Stream<GameSaveEntity> getGameSavesByUserEmail(String userEmail) {
        return gameSaveRepository.findGameSaveEntitiesByUserEmail(userEmail)
                .map(this::enrichGameSaveWithCachedData);
    }

    /**
     * Enrich the game save with cached data
     *
     * @param gameSave the game save
     * @return the game save with cached data
     */
    private GameSaveEntity enrichGameSaveWithCachedData(GameSaveEntity gameSave) {
        if (currencyCache.isEnabled()) {
            Optional<Currency> optionalCurrency = currencyCache.get(gameSave.getId());
            optionalCurrency.ifPresent(gameSave::setCurrencyEntity);
        }
        if (stageCache.isEnabled()) {
            Optional<Stage> optionalStage = stageCache.get(gameSave.getId());
            optionalStage.ifPresent(gameSave::setStageEntity);
        }

        return gameSave;
    }
}
