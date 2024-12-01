package com.lsadf.core.services.impl;

import static com.lsadf.core.constants.JsonAttributes.GameSave.NICKNAME;
import static com.lsadf.core.constants.JsonAttributes.GameSave.USER_EMAIL;
import static com.lsadf.core.constants.JsonAttributes.ID;
import static com.lsadf.core.constants.JsonAttributes.User.*;

import com.lsadf.core.entities.GameSaveEntity;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.common.Filter;
import com.lsadf.core.requests.game_save.GameSaveOrderBy;
import com.lsadf.core.requests.search.SearchRequest;
import com.lsadf.core.requests.user.UserOrderBy;
import com.lsadf.core.services.GameSaveService;
import com.lsadf.core.services.SearchService;
import com.lsadf.core.services.UserService;
import com.lsadf.core.utils.StreamUtils;
import java.util.List;
import java.util.stream.Stream;

/** Implementation of SearchService */
public class SearchServiceImpl implements SearchService {

  private final UserService userService;
  private final GameSaveService gameSaveService;

  public SearchServiceImpl(UserService userService, GameSaveService gameSaveService) {
    this.userService = userService;
    this.gameSaveService = gameSaveService;
  }

  /** {@inheritDoc} */
  @Override
  public Stream<User> searchUsers(SearchRequest searchRequest, UserOrderBy orderBy) {
    Stream<User> userStream = userService.getUsers();
    List<Filter> filters = searchRequest.getFilters();
    for (Filter filter : filters) {
      switch (filter.getType()) {
        case FIRST_NAME ->
            userStream = userStream.filter(user -> user.getFirstName().equals(filter.getValue()));
        case LAST_NAME ->
            userStream = userStream.filter(user -> user.getLastName().equals(filter.getValue()));
        case USERNAME, USER_EMAIL ->
            userStream = userStream.filter(user -> user.getUsername().equals(filter.getValue()));
        case ID -> userStream = userStream.filter(user -> user.getId().equals(filter.getValue()));
        case USER_ROLES ->
            userStream =
                userStream.filter(
                    user ->
                        user.getUserRoles().stream()
                            .anyMatch(role -> role.equals(filter.getValue())));
        default -> throw new IllegalArgumentException("Invalid filter type");
      }
    }

    return StreamUtils.sortUsers(userStream, orderBy);
  }

  /** {@inheritDoc} */
  @Override
  public Stream<GameSaveEntity> searchGameSaves(
      SearchRequest searchRequest, GameSaveOrderBy orderBy) {
    Stream<GameSaveEntity> gameSaveStream = gameSaveService.getGameSaves();
    List<Filter> filters = searchRequest.getFilters();
    for (Filter filter : filters) {
      switch (filter.getType()) {
        case ID ->
            gameSaveStream =
                gameSaveStream.filter(gameSave -> gameSave.getId().equals(filter.getValue()));
        case USER_EMAIL ->
            gameSaveStream =
                gameSaveStream.filter(
                    gameSave -> gameSave.getUserEmail().equals(filter.getValue()));
        case NICKNAME ->
            gameSaveStream =
                gameSaveStream.filter(gameSave -> gameSave.getNickname().equals(filter.getValue()));
        default -> throw new IllegalArgumentException("Invalid filter type");
      }
    }

    return StreamUtils.sortGameSaves(gameSaveStream, orderBy);
  }
}
