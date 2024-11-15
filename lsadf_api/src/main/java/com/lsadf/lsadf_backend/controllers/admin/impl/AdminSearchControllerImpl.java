package com.lsadf.lsadf_backend.controllers.admin.impl;

import com.lsadf.lsadf_backend.controllers.admin.AdminSearchController;
import com.lsadf.lsadf_backend.controllers.impl.BaseController;
import com.lsadf.core.entities.GameSaveEntity;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.GameSave;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.game_save.GameSaveOrderBy;
import com.lsadf.core.requests.search.SearchRequest;
import com.lsadf.core.requests.user.UserOrderBy;
import com.lsadf.core.responses.GenericResponse;
import com.lsadf.core.services.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

import static com.lsadf.core.utils.ResponseUtils.generateResponse;

/**
 * The implementation of the AdminSearchController
 */
@Slf4j
@RestController
public class AdminSearchControllerImpl extends BaseController implements AdminSearchController {

    private final SearchService searchService;
    private final Mapper mapper;


    @Autowired
    public AdminSearchControllerImpl(SearchService searchService,
                                     Mapper mapper) {
        this.searchService = searchService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return log;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<User>>> searchUsers(Jwt jwt,
                                                             SearchRequest searchRequest,
                                                             String orderBy) {
        UserOrderBy userOrderBy = orderBy != null ? UserOrderBy.valueOf(orderBy) : UserOrderBy.NONE;
        validateUser(jwt);
        try (Stream<User> userStream = searchService.searchUsers(searchRequest, userOrderBy)) {
            List<User> users = userStream.toList();
            return generateResponse(HttpStatus.OK, users);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<GameSave>>> searchGameSaves(Jwt jwt,
                                                                     SearchRequest searchRequest,
                                                                     String orderBy) {
        GameSaveOrderBy gameSaveOrderBy = orderBy != null ? GameSaveOrderBy.valueOf(orderBy) : GameSaveOrderBy.NONE;
        validateUser(jwt);
        try (Stream<GameSaveEntity> gameSaveStream = searchService.searchGameSaves(searchRequest, gameSaveOrderBy)) {
            List<GameSave> gameSaves = gameSaveStream
                    .map(mapper::mapGameSaveEntityToGameSave)
                    .toList();
            return generateResponse(HttpStatus.OK, gameSaves);
        }
    }
}
