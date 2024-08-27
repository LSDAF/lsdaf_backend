package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.entities.GoldEntity;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.repositories.GoldRepository;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.GoldService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class GoldServiceImpl implements GoldService {

    private final GoldRepository goldRepository;
    private final CacheService cacheService;
    private final GameSaveService gameSaveService;

    private final boolean cacheEnabled;

    public GoldServiceImpl(GoldRepository goldRepository,
                           CacheService cacheService,
                           GameSaveService gameSaveService) {
        this.goldRepository = goldRepository;
        this.cacheService = cacheService;
        this.gameSaveService = gameSaveService;

        this.cacheEnabled = cacheService.isCacheEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public long getGold(String gameSaveId) throws NotFoundException {
        if (cacheEnabled) {
            Optional<Long> optionalCachedGold = cacheService.getGold(gameSaveId);
            if (optionalCachedGold.isPresent()) {
                return optionalCachedGold.get();
            }
        }
        return goldRepository.findById(gameSaveId)
                .orElseThrow(() -> new NotFoundException("Gold not found for game save " + gameSaveId))
                .getGoldAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveGold(String gameSaveId, long gold, boolean toCache) throws NotFoundException {
        if (toCache) {
            cacheService.setGold(gameSaveId, gold);
        } else {
            saveGoldToDatabase(gameSaveId, gold);
        }
    }

    private void saveGoldToDatabase(String gameSaveId, long gold) throws NotFoundException {
        GoldEntity goldEntity = goldRepository.findById(gameSaveId)
                .orElseThrow(() -> new NotFoundException("Gold not found for game save " + gameSaveId));
        goldEntity.setGoldAmount(gold);
        goldRepository.save(goldEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getGems(String userId) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveGems(String userId, long gems) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkGameSaveOwnership(String gameSaveId, String userEmail) throws ForbiddenException, NotFoundException {
        if (cacheEnabled) {
            Optional<String> cacheUserEmail = cacheService.getGameSaveOwnership(gameSaveId);
            if (cacheUserEmail.isEmpty()) {
                gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
                return;
            }

            if (!cacheUserEmail.get().equals(userEmail)) {
                throw new ForbiddenException("User does not own game save " + gameSaveId);
            }
        } else {
            gameSaveService.checkGameSaveOwnership(gameSaveId, userEmail);
        }
    }
}
