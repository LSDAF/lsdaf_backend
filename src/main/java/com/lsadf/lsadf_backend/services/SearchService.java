package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;

import java.util.stream.Stream;

public interface SearchService {
    Stream<UserEntity> searchUsers(SearchRequest searchRequest, UserOrderBy orderBy);
    Stream<GameSaveEntity> searchGameSaves(SearchRequest searchRequest, GameSaveOrderBy orderBy);
}
