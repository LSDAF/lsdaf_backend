package com.lsadf.lsadf_backend.controllers;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for currency related operations.
 */
@RequestMapping(value = ControllerConstants.CURRENCY)
@Tag(name = ControllerConstants.Swagger.CURRENCY_CONTROLLER)
public interface CurrencyController {

    String GAME_SAVE_ID = "game_save_id";
    String GOLD = "gold";
    String DIAMOND = "diamond";
    String EMERALD = "emerald";
    String AMETHYST = "amethyst";

    @PostMapping(value = ControllerConstants.Currency.GAME_SAVE_ID)
    @Operation(summary = "Saves one or several currency amounts for a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> saveCurrency(LocalUser localUser,
                                                       String gameSaveId,
                                                       CurrencyRequest currencyRequest);

    @GetMapping(value = ControllerConstants.Currency.GAME_SAVE_ID)
    @Operation(summary = "Gets the currency amounts for a game save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<Void>> getCurrency(LocalUser localUser,
                                                      String gameSaveId);
}
