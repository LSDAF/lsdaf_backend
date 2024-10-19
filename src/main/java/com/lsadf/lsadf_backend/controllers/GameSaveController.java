package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

/**
 * Controller for game save operations
 */
@RequestMapping(value = ControllerConstants.GAME_SAVE)
@Tag(name = ControllerConstants.Swagger.GAME_SAVE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
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
    ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(Jwt jwt);

    /**
     * Updates a game nickname in function of its id
     *
     * @param gameSaveId the id of the game save
     * @param gameSaveUpdateNicknameRequest the nickname update request
     * @return
     */
    @PostMapping(value = ControllerConstants.GameSave.UPDATE_NICKNAME)
    @Operation(summary = "Updates the nickname of a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "404", description = ResponseMessages.NOT_FOUND),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    ResponseEntity<GenericResponse<Void>> updateNickname(Jwt jwt, String gameSaveId, GameSaveUpdateNicknameRequest gameSaveUpdateNicknameRequest);
}
