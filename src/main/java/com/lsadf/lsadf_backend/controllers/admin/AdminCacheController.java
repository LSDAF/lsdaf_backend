package com.lsadf.lsadf_backend.controllers.admin;

import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;

@RequestMapping(value = ControllerConstants.ADMIN_CACHE)
@Tag(name = ControllerConstants.Swagger.ADMIN_CACHE_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminCacheController {

    /**
     * Clears all the caches of the application
     *
     * @param jwt the requester JWT
     * @return empty response
     */
    @PutMapping(value = ControllerConstants.AdminCache.FLUSH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Clears all the caches of the application")
    ResponseEntity<GenericResponse<Void>> flushAndClearCache(@AuthenticationPrincipal Jwt jwt);

    /**
     * Checks if the cache is enabled
     *
     * @param jwt the requester JWT
     * @return true if the cache is enabled, false otherwise
     */
    @GetMapping(value = ControllerConstants.AdminCache.ENABLED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Checks if the cache is enabled")
    ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(@AuthenticationPrincipal Jwt jwt);

    /**
     * Enables/Disables the cache
     *
     * @param jwt the requester JWT
     * @return empty response
     */
    @PutMapping(value = ControllerConstants.AdminCache.TOGGLE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @Operation(summary = "Enables/Disables the cache")
    ResponseEntity<GenericResponse<Boolean>> toggleRedisCacheEnabling(@AuthenticationPrincipal Jwt jwt);
}
