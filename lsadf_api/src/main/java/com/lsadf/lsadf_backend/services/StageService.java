package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.Stage;

/**
 * Service for managing stages.
 */
public interface StageService {

    /**
     * Get the stage for the given game save id.
     * @param gameSaveId the game save id
     * @return the stage
     * @throws NotFoundException if the stage is not found
     */
    Stage getStage(String gameSaveId) throws NotFoundException;

    /**
     * Save the stage for the given game save id.
     * @param gameSaveId the game save id
     * @param stage the stage to save
     * @param toCache whether to save to cache
     * @throws NotFoundException if the stage is not found
     */
    void saveStage(String gameSaveId, Stage stage, boolean toCache) throws NotFoundException;
}