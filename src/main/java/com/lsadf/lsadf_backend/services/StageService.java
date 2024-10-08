package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.Stage;

public interface StageService {

    Stage getStage(String gameSaveId) throws NotFoundException;

    void saveStage(String gameSaveId, Stage stage, boolean toCache) throws NotFoundException;
}
