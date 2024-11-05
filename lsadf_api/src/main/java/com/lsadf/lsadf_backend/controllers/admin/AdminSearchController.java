package com.lsadf.lsadf_backend.controllers.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.constants.JsonViews;
import com.lsadf.lsadf_backend.constants.ResponseMessages;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.lsadf_backend.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;
import static com.lsadf.lsadf_backend.constants.ControllerConstants.Params.ORDER_BY;

@RequestMapping(value = ControllerConstants.ADMIN_SEARCH)
@Tag(name = ControllerConstants.Swagger.ADMIN_SEARCH_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminSearchController {


    /**
     * Searches for users in function of the given search criteria
     *
     * @param jwt           the requester JWT
     * @param searchRequest the search criteria
     * @param orderBy       the sorting order if any
     * @return the list of users
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "400", description = ResponseMessages.BAD_REQUEST),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = ControllerConstants.AdminSearch.SEARCH_USERS)
    @Operation(summary = "Searches for users in function of the give search criteria")
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<List<User>>> searchUsers(@AuthenticationPrincipal Jwt jwt,
                                                            @Valid @RequestBody(required = false) SearchRequest searchRequest,
                                                            @RequestParam(value = ORDER_BY, required = false) String orderBy);

    /**
     * Searches for game saves in function of the given search criteria
     *
     * @param jwt           the requester JWT
     * @param searchRequest the search criteria
     * @param orderBy
     * @return the list of game saves
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = ResponseMessages.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ResponseMessages.FORBIDDEN),
            @ApiResponse(responseCode = "200", description = ResponseMessages.OK),
            @ApiResponse(responseCode = "400", description = ResponseMessages.BAD_REQUEST),
            @ApiResponse(responseCode = "500", description = ResponseMessages.INTERNAL_SERVER_ERROR)
    })
    @PostMapping(value = ControllerConstants.AdminSearch.SEARCH_GAME_SAVES)
    @Operation(summary = "Searches for game saves in function of the give search criteria")
    @JsonView(JsonViews.Admin.class)
    ResponseEntity<GenericResponse<List<GameSave>>> searchGameSaves(@AuthenticationPrincipal Jwt jwt,
                                                              @Valid @RequestBody(required = false) SearchRequest searchRequest,
                                                              @RequestParam(value = ORDER_BY, required = false) String orderBy);
}
