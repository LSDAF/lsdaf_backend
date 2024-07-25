package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.configurations.CurrentUser;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for game save operations
 */
@RequestMapping(value = ControllerConstants.GAME_SAVE)
@Tag(name = ControllerConstants.Swagger.GAME_SAVE_CONTROLLER)
public interface GameSaveController {

    String GAME_SAVE_ID = "game_save_id";

    /**
     * Generates a new game, returns the generated game save
     *
     * @return the generated game save
     */
    @PostMapping(value = ControllerConstants.GameSave.GENERATE)
    @Operation(summary = "Generates a new game, returns the generated game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@CurrentUser LocalUser localUser);

    /**
     * Updates a game in function of its id
     * @param gameSaveId the id of the game save
     * @param save       the game save to update
     * @return
     */
    @PostMapping(value = ControllerConstants.GameSave.GAME_SAVE_ID)
    @Operation(summary = "Updates a game in function of its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<Void>> saveGame(@CurrentUser LocalUser localUser, String gameSaveId, GameSaveUpdateRequest request);
}
