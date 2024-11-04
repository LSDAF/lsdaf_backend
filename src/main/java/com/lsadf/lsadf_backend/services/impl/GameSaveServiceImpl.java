package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.lsadf_backend.exceptions.AlreadyTakenNicknameException;
import com.lsadf.lsadf_backend.exceptions.http.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.StageRepository;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
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
    private final StageRepository stageRepository;
    private final CurrencyRepository currencyRepository;

    private final Cache<String> gameSaveOwnershipCache;
    private final HistoCache<Stage> stageCache;
    private final HistoCache<Currency> currencyCache;


    public GameSaveServiceImpl(UserService userService,
                               GameSaveRepository gameSaveRepository,
                               StageRepository stageRepository,
                               CurrencyRepository currencyRepository,
                               Cache<String> gameSaveOwnershipCache,
                               HistoCache<Stage> stageCache,
                               HistoCache<Currency> currencyCache) {
        this.userService = userService;
        this.gameSaveRepository = gameSaveRepository;
        this.currencyRepository = currencyRepository;
        this.stageRepository = stageRepository;
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

        User user = userService.getUserByUsername(userEmail);

        GameSaveEntity entity = GameSaveEntity.builder()
                .userEmail(userEmail)
                .build();

        GameSaveEntity saved = gameSaveRepository.save(entity);
        saved.setNickname(saved.getId());

        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .userEmail(user.getUsername())
                .id(saved.getId())
                .build();

        saved.setCurrencyEntity(currencyEntity);

        StageEntity stageEntity = StageEntity.builder()
                .userEmail(user.getUsername())
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
        GameSaveEntity gameSaveEntity = getGameSaveEntity(saveId);
        return enrichGameSaveWithCachedData(gameSaveEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSaveEntity createGameSave(AdminGameSaveCreationRequest creationRequest) throws NotFoundException, AlreadyExistingGameSaveException {

        User user = userService.getUserByUsername(creationRequest.getUserEmail());

        GameSaveEntity entity = GameSaveEntity.builder()
                .healthPoints(creationRequest.getHealthPoints())
                .attack(creationRequest.getAttack())
                .userEmail(user.getUsername())
                .build();

        if (creationRequest.getId() != null) {
            if (gameSaveRepository.existsById(creationRequest.getId())) {
                throw new AlreadyExistingGameSaveException("Game save with id " + creationRequest.getId() + " already exists");
            }
            entity.setId(creationRequest.getId());
        }

        GameSaveEntity saved = gameSaveRepository.save(entity);

        String nickname = creationRequest.getNickname() != null ? creationRequest.getNickname() : saved.getId();

        saved.setNickname(nickname);

        CurrencyRequest currencyRequest = creationRequest.getCurrency();
        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .userEmail(user.getUsername())
                .id(saved.getId())
                .goldAmount(currencyRequest.getGold())
                .diamondAmount(currencyRequest.getDiamond())
                .emeraldAmount(currencyRequest.getEmerald())
                .amethystAmount(currencyRequest.getAmethyst())
                .build();

        saved.setCurrencyEntity(currencyEntity);

        StageRequest stageRequest = creationRequest.getStage();
        StageEntity stageEntity = StageEntity.builder()
                .currentStage(stageRequest.getCurrentStage())
                .maxStage(stageRequest.getMaxStage())
                .userEmail(user.getUsername())
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
        GameSaveEntity currentGameSave = getGameSaveEntity(saveId);

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
        GameSaveEntity currentGameSave = getGameSaveEntity(saveId);

        if (gameSaveRepository.findGameSaveEntityByNickname(updateRequest.getNickname()).isPresent()) {
            throw new AlreadyTakenNicknameException("Game save with nickname " + updateRequest.getNickname() + " already exists");
        }

        return updateGameSaveEntity(currentGameSave, updateRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String gameSaveId) {
        return gameSaveRepository.existsById(gameSaveId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteGameSave(String saveId) {
        if (saveId == null) {
            throw new NotFoundException("Game save id is null");
        }
        if (!gameSaveRepository.existsById(saveId)) {
            log.error("Game save with id {} not found", saveId);
            throw new NotFoundException("Game save with id " + saveId + " not found");
        }
        // Delete entities from currency & stage before deleting the game save
        currencyRepository.deleteById(saveId);
        stageRepository.deleteById(saveId);

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
            GameSaveEntity gameSaveEntity = getGameSaveEntity(saveId);

            if (!Objects.equals(gameSaveEntity.getUserEmail(), userEmail)) {
                throw new ForbiddenException("The given user email is not the owner of the game save");
            }

            return;
        }

        Optional<String> optionalOwnership = gameSaveOwnershipCache.get(saveId);
        if (optionalOwnership.isEmpty()) {
            GameSaveEntity gameSaveEntity = getGameSaveEntity(saveId);
            gameSaveOwnershipCache.set(saveId, userEmail);
            if (!Objects.equals(gameSaveEntity.getUserEmail(), userEmail)) {
                throw new ForbiddenException("The given user username is not the owner of the game save");
            }
            return;
        }

        if (!Objects.equals(optionalOwnership.get(), userEmail)) {
            throw new ForbiddenException("The given username is not the owner of the game save");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stream<GameSaveEntity> getGameSavesByUsername(String username) {
        if (!userService.checkUsernameExists(username)) {
            throw new NotFoundException("User with username " + username + " not found");
        }
        return gameSaveRepository.findGameSaveEntitiesByUserEmail(username)
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

    /**
     * Get the game save entity in the database or throw an exception if not found
     *
     * @param saveId the save id
     * @return the game save entity
     */
    private GameSaveEntity getGameSaveEntity(String saveId) {
        return gameSaveRepository.findById(saveId)
                .orElseThrow(() -> new NotFoundException("Game save with id " + saveId + " not found"));
    }
}
