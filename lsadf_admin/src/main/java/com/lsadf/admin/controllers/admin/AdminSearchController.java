package com.lsadf.admin.controllers.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.constants.JsonViews;
import com.lsadf.core.constants.ResponseMessages;
import com.lsadf.core.controllers.Controller;
import com.lsadf.core.models.GameSave;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.search.SearchRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.lsadf.core.configurations.SwaggerConfiguration.BEARER_AUTHENTICATION;
import static com.lsadf.core.configurations.SwaggerConfiguration.OAUTH2_AUTHENTICATION;
import static com.lsadf.core.constants.ControllerConstants.Params.ORDER_BY;

@RequestMapping(value = ControllerConstants.ADMIN_SEARCH)
@Tag(name = ControllerConstants.Swagger.ADMIN_SEARCH_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@SecurityRequirement(name = OAUTH2_AUTHENTICATION)
public interface AdminSearchController extends Controller {


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