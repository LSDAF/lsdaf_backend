package com.lsadf.lsadf_backend.services;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.requests.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.UserOrderBy;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.search.FilterRequest;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.constants.JsonAttributes.GameSave.*;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.ID;
import static com.lsadf.lsadf_backend.constants.JsonAttributes.User.*;

@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserService userService;
    private final GameSaveService gameSaveService;
    private final Mapper mapper;

    public AdminServiceImpl(UserService userService,
                            GameSaveService gameSaveService,
                            Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
        this.gameSaveService = gameSaveService;
    }

    @Override
    public GlobalInfo getGlobalInfo() {
        return null;
    }

    @Override
    public List<User> getUsers(UserOrderBy orderBy) {
        Stream<UserEntity> stream = userService.getUsers();
        stream = StreamUtils.sortUsers(stream, Optional.of(orderBy));

        return stream
                .map(mapper::mapToUser)
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(String userId) {;
        return mapper.mapToUser(userService.getUserById(userId));
    }

    @Override
    public User createUser(UserCreationRequest creationRequest) {
        UserEntity user = userService.createUser(creationRequest);

        return mapper.mapToUser(user);
    }

    @Override
    public User updateUser(String userId, User user, String userEmail) {
        return mapper.mapToUser(userService.updateUser(userId, user.getName(), userEmail));
    }

    @Override
    public void deleteUser(String userId, String userEmail) {

    }

    @Override
    public List<GameSave> getGameSaves(GameSaveOrderBy orderBy) {
        Stream<GameSaveEntity> stream = gameSaveService.getGameSaves();
        stream = StreamUtils.sortGameSaves(stream, Optional.of(orderBy));

        return stream
                .map(mapper::mapToGameSave)
                .collect(Collectors.toList());
    }

    @Override
    public GameSave getGameSave(String saveId, String userEmail) throws ForbiddenException, UnauthorizedException, NotFoundException {
        return gameSaveService.getGameSave(saveId, userEmail);
    }

    @Override
    public GameSave updateGameSave(String saveId, GameSave newSaveGame, String userEmail) throws ForbiddenException, UnauthorizedException, NotFoundException {
        return gameSaveService.updateGameSave(saveId, newSaveGame, userEmail);
    }

    @Override
    public void deleteGameSave(String saveId, String userEmail) throws ForbiddenException, NotFoundException {
        gameSaveService.deleteGameSave(saveId, userEmail);
    }

    @Override
    public GameSave createGameSave(AdminGameSaveCreationRequest adminGameSaveCreationRequest) {
        return null;
    }

    @Override
    public List<User> searchUsers(SearchRequest searchRequest) {
        Stream<UserEntity> userStream = userService.getUsers();
        List<FilterRequest> filters = searchRequest.getFilters();
        for (FilterRequest filter : filters) {
            switch (filter.getType()) {
                case NAME -> userStream = userStream.filter(user -> user.getName().equals(filter.getValue()));
                case ID -> userStream = userStream.filter(user -> user.getId().equals(filter.getValue()));
                case EMAIL -> userStream = userStream.filter(user -> user.getEmail().equals(filter.getValue()));
                case PROVIDER -> userStream = userStream.filter(user -> user.getProvider().equals(filter.getValue()));
            }
        }
        return userStream
                .map(mapper::mapToUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameSave> searchGameSaves(SearchRequest searchRequest) {
        Stream<GameSaveEntity> gameSaveStream = gameSaveService.getGameSaves();
        List<FilterRequest> filters = searchRequest.getFilters();
        for (FilterRequest filter : filters) {
            switch (filter.getType()) {
                case USER_ID -> gameSaveStream = gameSaveStream.filter(gameSave -> gameSave.getUser().getId().equals(filter.getValue()));
                case ID -> gameSaveStream = gameSaveStream.filter(gameSave -> gameSave.getId() == filter.getValue());
            }
        }

        return gameSaveStream
                .map(mapper::mapToGameSave)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(String userEmail) {
        UserEntity user = userService.getUserByEmail(userEmail);

        return mapper.mapToUser(user);
    }
}
