package com.lsadf.lsadf_backend.services;

import com.lsadf.core.entities.GameSaveEntity;
import com.lsadf.core.models.User;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;

import java.util.stream.Stream;

public interface SearchService {
    /**
     * Search for users based on the given search request
     *
     * @param searchRequest The search request
     * @param orderBy       The order by
     * @return A stream of users
     */
    Stream<User> searchUsers(SearchRequest searchRequest, UserOrderBy orderBy);


    default Stream<User> searchUsers(SearchRequest searchRequest) {
        return searchUsers(searchRequest, UserOrderBy.NONE);
    }

    /**
     * Search for game saves based on the given search request
     *
     * @param searchRequest The search request
     * @param orderBy       The order by
     * @return A stream of game saves
     */
    Stream<GameSaveEntity> searchGameSaves(SearchRequest searchRequest, GameSaveOrderBy orderBy);

    default Stream<GameSaveEntity> searchGameSaves(SearchRequest searchRequest) {
        return searchGameSaves(searchRequest, GameSaveOrderBy.NONE);
    }
}
