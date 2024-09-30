package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.exceptions.*;
import com.lsadf.lsadf_backend.services.*;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of AdminService
 */
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserService userService;
    private final GameSaveService gameSaveService;
    private final Mapper mapper;
    private final SearchService searchService;
    private final CacheService localCacheService;
    private final CacheService redisCacheService;
    private final CacheFlushService cacheFlushService;
    private final ClockService clockService;

    public AdminServiceImpl(UserService userService,
                            GameSaveService gameSaveService,
                            Mapper mapper,
                            SearchService searchService,
                            CacheService localCacheService,
                            CacheService redisCacheService,
                            CacheFlushService cacheFlushService,
                            ClockService clockService) {
        this.userService = userService;
        this.mapper = mapper;
        this.searchService = searchService;
        this.gameSaveService = gameSaveService;
        this.localCacheService = localCacheService;
        this.redisCacheService = redisCacheService;
        this.cacheFlushService = cacheFlushService;
        this.clockService = clockService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GlobalInfo getGlobalInfo() {
        Long userCount = userService.getUsers().count();
        Long gameSaveCount = gameSaveService.getGameSaves().count();
        Instant now = clockService.nowInstant();

        return GlobalInfo.builder()
                .userCounter(userCount)
                .now(now)
                .gameSaveCounter(gameSaveCount)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flushAndClearCache() {
        log.info("Clearing all caches");
        cacheFlushService.flushCurrencies();
        localCacheService.clearCaches();
        redisCacheService.clearCaches();
    }

    @Override
    public void toggleCache() {
        redisCacheService.toggleCacheEnabling();
    }

    @Override
    public boolean isRedisCacheEnabled() {
        return redisCacheService.isEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers(UserOrderBy orderBy) {
        Stream<UserEntity> stream = userService.getUsers();
        if (orderBy != null && !orderBy.equals(UserOrderBy.NONE)) {
            stream = StreamUtils.sortUsers(stream, orderBy);
        }

        return stream
                .map(mapper::mapToUser)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GameSave getGameSave(String saveId) throws NotFoundException {
        return mapper.mapToGameSave(gameSaveService.getGameSave(saveId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSave updateGameSave(String saveId, AdminGameSaveUpdateRequest updateRequest) throws ForbiddenException, UnauthorizedException, NotFoundException, AlreadyTakenNicknameException {
        return mapper.mapToGameSave(gameSaveService.updateNickname(saveId, updateRequest));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserAdminDetails getUserByEmail(String userId) throws NotFoundException {
        return mapper.mapToUserAdminDetails(userService.getUserByEmail(userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(AdminUserCreationRequest creationRequest) throws AlreadyExistingUserException {
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                .email(creationRequest.getEmail())
                .name(creationRequest.getName())
                .password(creationRequest.getPassword())
                .userRoles(creationRequest.getUserRoles())
                .providerUserId(creationRequest.getProviderUserId())
                .userId(creationRequest.getUserId())
                .build();
        UserEntity user = userService.createUser(userCreationRequest);
        user.setEnabled(creationRequest.isEnabled());

        return mapper.mapToUser(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUser(String userId, AdminUserUpdateRequest userUpdateRequest) throws NotFoundException, AlreadyExistingUserException {
        return mapper.mapToUser(userService.updateUser(userId, userUpdateRequest));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(String userId) throws NotFoundException {
        userService.deleteUser(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserByEmail(String email) throws NotFoundException {
        userService.deleteUserByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameSave> getGameSaves(GameSaveOrderBy orderBy) {
        Stream<GameSaveEntity> stream = gameSaveService.getGameSaves();
        if (orderBy != null) {
            stream = StreamUtils.sortGameSaves(stream, orderBy);
        }

        return stream
                .map(mapper::mapToGameSave)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteGameSave(String saveId) throws NotFoundException {
        gameSaveService.deleteGameSave(saveId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameSave createGameSave(AdminGameSaveCreationRequest adminGameSaveCreationRequest) throws NotFoundException, AlreadyExistingGameSaveException {
        GameSaveEntity entity = gameSaveService.createGameSave(adminGameSaveCreationRequest);
        return mapper.mapToGameSave(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsers(SearchRequest searchRequest, UserOrderBy orderBy) {
        Stream<UserEntity> userStream = searchService.searchUsers(searchRequest, orderBy);
        userStream = StreamUtils.sortUsers(userStream, orderBy);
        return userStream
                .map(mapper::mapToUser)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameSave> searchGameSaves(SearchRequest searchRequest, GameSaveOrderBy orderBy) {
        Stream<GameSaveEntity> gameSaveStream = searchService.searchGameSaves(searchRequest, orderBy);
        gameSaveStream = StreamUtils.sortGameSaves(gameSaveStream, orderBy);
        return gameSaveStream
                .map(mapper::mapToGameSave)
                .collect(Collectors.toList());
    }



    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserAdminDetails getUserById(String userId) throws NotFoundException {
        UserEntity user = userService.getUserById(userId);
        return mapper.mapToUserAdminDetails(user);
    }
}
