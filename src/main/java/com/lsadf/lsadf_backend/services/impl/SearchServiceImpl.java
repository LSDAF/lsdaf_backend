package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.common.Filter;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.SearchService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.StreamUtils;

import java.util.List;
import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.ID;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

/**
 * Implementation of SearchService
 */
public class SearchServiceImpl implements SearchService {

    private final UserService userService;
    private final GameSaveService gameSaveService;

    public SearchServiceImpl(UserService userService,
                             GameSaveService gameSaveService) {
        this.userService = userService;
        this.gameSaveService = gameSaveService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<User> searchUsers(SearchRequest searchRequest, UserOrderBy orderBy) {
        Stream<User> userStream = userService.getUsers();
        List<Filter> filters = searchRequest.getFilters();
        for (Filter filter : filters) {
            switch (filter.getType()) {
                case FIRST_NAME -> userStream = userStream.filter(user -> user.getFirstName().equals(filter.getValue()));
                case LAST_NAME -> userStream = userStream.filter(user -> user.getLastName().equals(filter.getValue()));
                case ID -> userStream = userStream.filter(user -> user.getId().equals(filter.getValue()));
                case USERNAME -> userStream = userStream.filter(user -> user.getUsername().equals(filter.getValue()));
                case USER_ROLES -> userStream = userStream.filter(user -> user.getUserRoles().stream().anyMatch(role -> role.equals(filter.getValue())));
                default -> throw new IllegalArgumentException("Invalid filter type");
            }
        }

        return StreamUtils.sortUsers(userStream, orderBy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<GameSaveEntity> searchGameSaves(SearchRequest searchRequest, GameSaveOrderBy orderBy) {
        Stream<GameSaveEntity> gameSaveStream = gameSaveService.getGameSaves();
        List<Filter> filters = searchRequest.getFilters();
        for (Filter filter : filters) {
            switch (filter.getType()) {
                case ID -> gameSaveStream = gameSaveStream.filter(gameSave -> gameSave.getId().equals(filter.getValue()));
                default -> throw new IllegalArgumentException("Invalid filter type");
            }
        }

        return StreamUtils.sortGameSaves(gameSaveStream, orderBy);
    }
}
