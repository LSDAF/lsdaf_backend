package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.annotations.Uuid;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.requests.inventory.InventoryRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

/**
 * Controller for inventory related operations.
 */
@RequestMapping(value = ControllerConstants.INVENTORY)
@Tag(name = ControllerConstants.Swagger.INVENTORY_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface InventoryController {

    String GAME_SAVE_ID = "game_save_id";

    @PostMapping(value = ControllerConstants.Inventory.GAME_SAVE_ID)
    @Operation(summary = "Saves the inventory for a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> saveInventory(@AuthenticationPrincipal Jwt jwt,
                                                        @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                        @Valid @RequestBody InventoryRequest inventoryRequest);

    @GetMapping(value = ControllerConstants.Inventory.GAME_SAVE_ID)
    @Operation(summary = "Gets the inventory for a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> getInventory(@AuthenticationPrincipal Jwt jwt,
                                                       @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId);
}
