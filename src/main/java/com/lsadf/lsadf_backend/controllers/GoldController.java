package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.models.Gold;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;

/**
 * Controller for game save gold operations
 */
@RequestMapping(value = ControllerConstants.GOLD)
@Tag(name = ControllerConstants.Swagger.GOLD_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
public interface GoldController {

    String GAME_SAVE_ID = "game_save_id";
    String GOLD_AMOUNT = "gold_amount";

    /**
     * Gets the gold amount for a game save
     *
     * @param gameSaveId the id of the game save
     * @return the gold object
     */
    @GetMapping(value = ControllerConstants.Gold.GAME_SAVE_ID)
    @Operation(summary = "Gets the gold amount for a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Gold>> getGold(LocalUser localUser, String gameSaveId);

    /**
     * Saves new amount of gold for a game save
     *
     * @param gameSaveId the id of the game save
     * @param amount     the amount of gold to save
     * @return generic response
     */
    @PutMapping(value = ControllerConstants.Gold.GAME_SAVE_ID)
    @Operation(summary = "Saves new amount of gold for a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> saveGold(LocalUser localUser, String gameSaveId, long amount);
}
