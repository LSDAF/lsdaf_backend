package com.lsadf.lsadf_backend.controllers;

import com.lsadf.core.annotations.Uuid;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.requests.item.ItemRequest;
import com.lsadf.core.responses.GenericResponse;
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

import static com.lsadf.core.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.core.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

/**
 * Controller for inventory related operations.
 */
@RequestMapping(value = ControllerConstants.INVENTORY)
@Tag(name = ControllerConstants.Swagger.INVENTORY_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface InventoryController {

    String GAME_SAVE_ID = "game_save_id";
    String ITEM_ID = "item_id";

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

    @PostMapping(value = ControllerConstants.Inventory.ITEMS)
    @Operation(summary = "Creates an item in the inventory of a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> createItemInInventory(@AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                                @RequestBody @Valid ItemRequest itemRequest);

    @DeleteMapping(value = ControllerConstants.Inventory.ITEM_ID)
    @Operation(summary = "Deletes an item from the inventory of a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> deleteItemFromInventory(@AuthenticationPrincipal Jwt jwt,
                                                                  @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                                  @PathVariable(value = ITEM_ID) @Uuid String itemId);

    @PutMapping(value = ControllerConstants.Inventory.ITEM_ID)
    @Operation(summary = "Updates an item in the inventory of a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> updateItemInInventory(@AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable(value = GAME_SAVE_ID) @Uuid String gameSaveId,
                                                                @PathVariable(value = ITEM_ID) @Uuid String itemId,
                                                                @RequestBody @Valid ItemRequest itemRequest);
}
