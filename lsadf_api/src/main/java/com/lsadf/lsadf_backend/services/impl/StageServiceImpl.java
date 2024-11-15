package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.core.cache.Cache;
import com.lsadf.core.entities.StageEntity;
import com.lsadf.core.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.core.models.Stage;
import com.lsadf.lsadf_backend.repositories.StageRepository;
import com.lsadf.lsadf_backend.services.StageService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the stage service.
 */
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;
    private final Cache<Stage> stageCache;
    private final Mapper mapper;

    public StageServiceImpl(StageRepository stageRepository, Cache<Stage> stageCache, Mapper mapper) {
        this.stageRepository = stageRepository;
        this.stageCache = stageCache;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stage getStage(String gameSaveId) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }
        if (stageCache.isEnabled()) {
            Optional<Stage> optionalCachedStage = stageCache.get(gameSaveId);
            if (optionalCachedStage.isPresent()) {
                Stage stage = optionalCachedStage.get();
                if (stage.getMaxStage() == null
                        || stage.getCurrentStage() == null) {
                    StageEntity stageEntity = getStageEntity(gameSaveId);
                    return mergeStages(stage, stageEntity);
                }
                return stage;
            }
        }
        StageEntity stageEntity = getStageEntity(gameSaveId);
        return mapper.mapStageEntityToStage(stageEntity);
    }

    private StageEntity getStageEntity(String gameSaveId) {
        return stageRepository.findById(gameSaveId)
                .orElseThrow(() -> new NotFoundException("Stage not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveStage(String gameSaveId, Stage stage, boolean toCache) throws NotFoundException {
        if (gameSaveId == null) {
            throw new IllegalArgumentException("Game save id cannot be null");
        }
        if (stage == null || isStageNull(stage)) {
            throw new IllegalArgumentException("Stage cannot be null");
        }
        if (toCache) {
            stageCache.set(gameSaveId, stage);
        } else {
            saveStageToDatabase(gameSaveId, stage);
        }
    }

    /**
     * Save the stage to the database
     * @param gameSaveId the game save id
     * @param stage the stage to save
     */
    private void saveStageToDatabase(String gameSaveId, Stage stage) {
        StageEntity stageEntity = getStageEntity(gameSaveId);

        boolean hasUpdates = false;

        if (stage.getCurrentStage() != null && !stage.getCurrentStage().equals(stageEntity.getCurrentStage())) {
            stageEntity.setCurrentStage(stage.getCurrentStage());
            hasUpdates = true;
        }
        if (stage.getMaxStage() != null && !stage.getMaxStage().equals(stageEntity.getMaxStage())) {
            stageEntity.setMaxStage(stage.getMaxStage());
            hasUpdates = true;
        }

        if (hasUpdates) {
            stageRepository.save(stageEntity);
        }
    }

    /**
     * Merge the stage POJO with the stage entity from the database
     *
     * @param stage       the stage POJO
     * @param stageEntity the stage entity from the database
     * @return the merged stage POJO
     */
    private static Stage mergeStages(Stage stage, StageEntity stageEntity) {
        if (stage.getCurrentStage() == null) {
            stage.setCurrentStage(stageEntity.getCurrentStage());
        }
        if (stage.getMaxStage() == null) {
            stage.setMaxStage(stageEntity.getMaxStage());
        }
        return stage;
    }

    /**
     * Check if the stage is null
     * @param stage the stage
     * @return true if all the stage fields are null, false otherwise
     */
    private static boolean isStageNull(Stage stage) {
        return stage.getCurrentStage() == null
                && stage.getMaxStage() == null;
    }
}
