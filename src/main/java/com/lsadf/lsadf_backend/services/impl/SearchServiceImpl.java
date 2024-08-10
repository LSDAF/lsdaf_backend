package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.requests.common.Filter;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.SearchService;
import com.lsadf.lsadf_backend.services.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.USER_ID;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.ID;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

/**
 * Implementation of SearchService
 */
public class SearchServiceImpl implements SearchService {

    private final UserService userService;
    private final GameSaveService gameSaveService;

    public SearchServiceImpl(UserService userService, GameSaveService gameSaveService) {
        this.userService = userService;
        this.gameSaveService = gameSaveService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stream<UserEntity> searchUsers(SearchRequest searchRequest, UserOrderBy orderBy) {
        Stream<UserEntity> userStream = userService.getUsers();
        List<Filter> filters = searchRequest.getFilters();
        for (Filter filter : filters) {
            switch (filter.getType()) {
                case NAME -> userStream = userStream.filter(user -> user.getName().equals(filter.getValue()));
                case ID -> userStream = userStream.filter(user -> user.getId().equals(filter.getValue()));
                case EMAIL -> userStream = userStream.filter(user -> user.getEmail().equals(filter.getValue()));
                case PROVIDER -> userStream = userStream.filter(user -> user.getProvider().toString().equals(filter.getValue()));
                default -> throw new IllegalArgumentException("Invalid filter type");
            }
        }
        switch (orderBy) {
            case ID -> {
            }
            case ID_DESC -> {
            }
            case EMAIL -> {
            }
            case EMAIL_DESC -> {
            }
            case NAME -> {
            }
            case NAME_DESC -> {
            }
            case CREATED_AT -> {
            }
            case CREATED_AT_DESC -> {
            }
            case UPDATED_AT -> {
            }
            case UPDATED_AT_DESC -> {
            }
            case NONE -> {
            }
        }
        return userStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Stream<GameSaveEntity> searchGameSaves(SearchRequest searchRequest, GameSaveOrderBy orderBy) {
        Stream<GameSaveEntity> gameSaveStream = gameSaveService.getGameSaves();
        List<Filter> filters = searchRequest.getFilters();
        for (Filter filter : filters) {
            switch (filter.getType()) {
                case USER_ID -> gameSaveStream = gameSaveStream.filter(gameSave -> gameSave.getUser().getId().equals(filter.getValue()));
                case ID -> gameSaveStream = gameSaveStream.filter(gameSave -> gameSave.getId().equals(filter.getValue()));
                default -> throw new IllegalArgumentException("Invalid filter type");
            }
        }

        return gameSaveStream;
    }
}
