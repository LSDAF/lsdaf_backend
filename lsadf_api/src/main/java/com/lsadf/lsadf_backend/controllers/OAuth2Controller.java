package com.lsadf.lsadf_backend.controllers;

import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.models.JwtAuthentication;
import com.lsadf.core.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = ControllerConstants.OAUTH2)
@Tag(name = ControllerConstants.Swagger.OAUTH_2_CONTROLLER)
public interface OAuth2Controller {

    String CODE = "code";

    /**
     * Handle OAuth2 callback
     *
     * @param code OAuth2 code
     * @return GenericResponse with JwtAuthentication
     */
    @GetMapping(value = ControllerConstants.OAuth2.CALLBACK)
    @Operation(summary = "Handles the OAuth2 callback", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GenericResponse<JwtAuthentication>> handleOAuth2Callback(String code);
}
