package com.lsadf.core.services;

import com.lsadf.core.models.GameSave;
import com.lsadf.core.models.User;
import com.lsadf.core.requests.game_save.GameSaveSortingParameter;
import com.lsadf.core.requests.search.SearchRequest;
import com.lsadf.core.requests.user.UserSortingParameter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public interface SearchService {
  /**
   * Search for users based on the given search request
   *
   * @param searchRequest The search request
   * @param orderBy The order by
   * @return A stream of users
   */
  Stream<User> searchUsers(SearchRequest searchRequest, List<UserSortingParameter> orderBy);

  default Stream<User> searchUsers(SearchRequest searchRequest) {
    return searchUsers(searchRequest, Collections.singletonList(UserSortingParameter.NONE));
  }

  /**
   * Search for game saves based on the given search request
   *
   * @param searchRequest The search request
   * @param orderBy The order by
   * @return A stream of game saves
   */
  Stream<GameSave> searchGameSaves(
      SearchRequest searchRequest, List<GameSaveSortingParameter> orderBy);

  default Stream<GameSave> searchGameSaves(SearchRequest searchRequest) {
    return searchGameSaves(searchRequest, Collections.singletonList(GameSaveSortingParameter.NONE));
  }
}
