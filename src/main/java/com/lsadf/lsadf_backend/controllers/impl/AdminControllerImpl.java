package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.annotations.CurrentUser;
import com.lsadf.lsadf_backend.controllers.AdminController;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.*;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.*;
import com.lsadf.lsadf_backend.utils.StreamUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.LOCAL_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.REDIS_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.utils.ResponseUtils.generateResponse;

/**
 * Implementation of AdminController
 */
@Slf4j
@RestController
public class AdminControllerImpl extends BaseController implements AdminController {
    private final StageService stageService;
    private final CurrencyService currencyService;
    private final Mapper mapper;
    private final CacheService redisCacheService;
    private final UserService userService;
    private final GameSaveService gameSaveService;
    private final SearchService searchService;
    private final CacheService localCacheService;
    private final CacheFlushService cacheFlushService;
    private final ClockService clockService;
    private final EmailService emailService;

    @Autowired
    public AdminControllerImpl(StageService stageService,
                               CurrencyService currencyService,
                               Mapper mapper,
                               CacheService redisCacheService,
                               UserService userService,
                               GameSaveService gameSaveService,
                               SearchService searchService,
                               CacheService localCacheService,
                               CacheFlushService cacheFlushService,
                               ClockService clockService,
                               EmailService emailService) {
        this.stageService = stageService;
        this.currencyService = currencyService;
        this.mapper = mapper;
        this.redisCacheService = redisCacheService;
        this.userService = userService;
        this.gameSaveService = gameSaveService;
        this.searchService = searchService;
        this.localCacheService = localCacheService;
        this.cacheFlushService = cacheFlushService;
        this.clockService = clockService;
        this.emailService = emailService;
    }

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
    public ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            Long userCount = userService.getUsers().count();
            Long gameSaveCount = gameSaveService.getGameSaves().count();
            Instant now = clockService.nowInstant();

            GlobalInfo globalInfo = GlobalInfo.builder()
                    .userCounter(userCount)
                    .now(now)
                    .gameSaveCounter(gameSaveCount)
                    .build();
            return generateResponse(HttpStatus.OK, globalInfo);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting global info: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting global info: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            boolean cacheEnabled = redisCacheService.isEnabled();
            return generateResponse(HttpStatus.OK, cacheEnabled);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while checking cache status: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while checking cache status: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Boolean>> toggleRedisCacheEnabling(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);
            redisCacheService.toggleCacheEnabling();
            Boolean cacheEnabled = redisCacheService.isEnabled();
            return generateResponse(HttpStatus.OK, cacheEnabled);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while toggling cache: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while toggling cache: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<User>>> getUsers(@CurrentUser LocalUser localUser,
                                                                @RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        try {
            validateUser(localUser);
            try (Stream<UserEntity> stream = userService.getUsers()) {
                if (orderBy != null && !orderBy.equals(UserOrderBy.NONE)) {
                    Stream<UserEntity> orderedStream = StreamUtils.sortUsers(stream, orderBy);

                    List<User> users = orderedStream
                            .map(mapper::mapToUser)
                            .toList();

                    return generateResponse(HttpStatus.OK, users);
                }

                List<User> users = stream
                        .map(mapper::mapToUser)
                        .toList();

                return generateResponse(HttpStatus.OK, users);
            }
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting users: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserById(@CurrentUser LocalUser localUser,
                                                                                 @PathVariable(value = USER_ID) String userId) {
        try {
            validateUser(localUser);
            UserEntity user = userService.getUserById(userId);
            UserAdminDetails userAdminDetails = mapper.mapToUserAdminDetails(user);
            return generateResponse(HttpStatus.OK, userAdminDetails);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting user by id: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Error while getting user by id: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting user by id: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<UserAdminDetails>> getDetailedUserByEmail(@CurrentUser LocalUser localUser,
                                                                                    @PathVariable(value = USER_EMAIL) String userEmail) {
        try {
            validateUser(localUser);
            UserEntity user = userService.getUserByEmail(userEmail);
            UserAdminDetails userAdminDetails = mapper.mapToUserAdminDetails(user);
            return generateResponse(HttpStatus.OK, userAdminDetails);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting user by email: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Error while getting user by email: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting user by email: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> updateUser(@CurrentUser LocalUser localUser,
                                                            @PathVariable(value = USER_ID) String userId,
                                                            @Valid @RequestBody AdminUserUpdateRequest user) {
        try {
            validateUser(localUser);
            UserEntity updatedUserEntity = userService.updateUser(userId, user);
            User updatedUser = mapper.mapToUser(updatedUserEntity);
            return generateResponse(HttpStatus.OK, updatedUser);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while updating user: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Error while updating user: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while updating user: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> deleteUser(@CurrentUser LocalUser localUser,
                                                            @PathVariable(value = USER_ID) String userId) {
        try {
            validateUser(localUser);
            userService.deleteUser(userId);
            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while deleting user: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Error while deleting user: ", e);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception while deleting user: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> createUser(@CurrentUser LocalUser localUser,
                                                            @Valid @RequestBody AdminUserCreationRequest adminUserCreationRequest) {
        try {
            validateUser(localUser);
            UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                    .email(adminUserCreationRequest.getEmail())
                    .name(adminUserCreationRequest.getName())
                    .password(adminUserCreationRequest.getPassword())
                    .userRoles(adminUserCreationRequest.getUserRoles())
                    .providerUserId(adminUserCreationRequest.getProviderUserId())
                    .userId(adminUserCreationRequest.getUserId())
                    .build();
            UserEntity user = userService.createUser(userCreationRequest);
            user.setEnabled(adminUserCreationRequest.getEnabled());
            user.setVerified(adminUserCreationRequest.getVerified());

            User newUser = mapper.mapToUser(user);
            return generateResponse(HttpStatus.OK, newUser);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while creating user: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (AlreadyExistingUserException e) {
            log.error("Error while creating user: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while creating user: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(@CurrentUser LocalUser localUser,
                                                                        @RequestParam(value = ORDER_BY, required = false) GameSaveOrderBy orderBy) {
        try {
            validateUser(localUser);
            try (Stream<GameSaveEntity> stream = gameSaveService.getGameSaves()) {
                if (orderBy != null) {
                    Stream<GameSaveEntity> orderedStream = StreamUtils.sortGameSaves(stream, orderBy);
                    List<GameSave> gameSaves = orderedStream.map(mapper::mapToGameSave).toList();
                    return generateResponse(HttpStatus.OK, gameSaves);
                }

                List<GameSave> gameSaves = stream.map(mapper::mapToGameSave).toList();

                return generateResponse(HttpStatus.OK, gameSaves);
            }
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting game saves: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting game saves: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> getGameSave(@CurrentUser LocalUser localUser,
                                                                 @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(localUser);
            GameSaveEntity entity = gameSaveService.getGameSave(gameSaveId);
            GameSave gameSave = mapper.mapToGameSave(entity);
            return generateResponse(HttpStatus.OK, gameSave);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while getting game save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while getting game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> updateGameSave(@CurrentUser LocalUser localUser,
                                                                    @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                    @Valid @RequestBody AdminGameSaveUpdateRequest adminGameSaveUpdateRequest) {
        try {
            validateUser(localUser);
            GameSaveEntity gameSaveEntity = gameSaveService.updateNickname(gameSaveId, adminGameSaveUpdateRequest);
            GameSave gameSave = mapper.mapToGameSave(gameSaveEntity);
            return generateResponse(HttpStatus.OK, gameSave);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while updating game save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (AlreadyTakenNicknameException e) {
            log.error("AlreadyTakenNicknameException exception while saving game: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, "The given nickname is already taken.", null);
        } catch (Exception e) {
            log.error("Error while updating game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@CurrentUser LocalUser localUser,
                                                                         @Valid @RequestBody AdminGameSaveCreationRequest creationRequest) {
        try {
            validateUser(localUser);
            GameSaveEntity gameSaveEntity = gameSaveService.createGameSave(creationRequest);
            GameSave gameSave = mapper.mapToGameSave(gameSaveEntity);
            return generateResponse(HttpStatus.OK, gameSave);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while creating game save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (AlreadyExistingGameSaveException e) {
            log.error("Game Save with id {} already exists", creationRequest.getId());
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while creating game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> searchUsers(@CurrentUser LocalUser localUser,
                                                             @Valid @RequestBody SearchRequest searchRequest,
                                                             @RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        try {
            validateUser(localUser);
            try (Stream<UserEntity> userStream = searchService.searchUsers(searchRequest, orderBy)) {
                Stream<UserEntity> sortedStream = StreamUtils.sortUsers(userStream, orderBy);
                List<User> users = sortedStream
                        .map(mapper::mapToUser)
                        .toList();
                return generateResponse(HttpStatus.OK, users);
            }
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while searching users: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException exception while searching users: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while searching users: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> searchGameSaves(@CurrentUser LocalUser localUser,
                                                                     @Valid @RequestBody SearchRequest searchRequest,
                                                                     @RequestParam(value = ORDER_BY) GameSaveOrderBy orderBy) {
        try {
            validateUser(localUser);
            try (Stream<GameSaveEntity> gameSaveStream = searchService.searchGameSaves(searchRequest, orderBy)) {
                Stream<GameSaveEntity> sortedStream = StreamUtils.sortGameSaves(gameSaveStream, orderBy);
                List<GameSave> gameSaves = sortedStream
                        .map(mapper::mapToGameSave)
                        .toList();
                return generateResponse(HttpStatus.OK, gameSaves);
            }
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while searching game saves: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException exception while searching users: ", e);
            return generateResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while searching game saves: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> deleteGameSave(@CurrentUser LocalUser localUser,
                                                                @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(localUser);

            gameSaveService.deleteGameSave(gameSaveId);
            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while deleting game save: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while deleting game save: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> flushAndClearCache(@CurrentUser LocalUser localUser) {
        try {
            validateUser(localUser);

            log.info("Clearing all caches");
            cacheFlushService.flushCurrencies();
            cacheFlushService.flushStages();
            localCacheService.clearCaches();
            redisCacheService.clearCaches();

            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while clearing cache: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while clearing cache: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> updateGameSaveCurrency(@CurrentUser LocalUser localUser,
                                                                        @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                        @Valid @RequestBody CurrencyRequest currencyRequest) {
        try {
            validateUser(localUser);
            Currency currency = mapper.mapCurrencyRequestToCurrency(currencyRequest);
            currencyService.saveCurrency(gameSaveId, currency, redisCacheService.isEnabled());

            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while updating currency: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while updating currency: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> updateGameSaveStages(@CurrentUser LocalUser localUser,
                                                                      @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                      @Valid @RequestBody StageRequest stageRequest) {
        try {
            validateUser(localUser);
            Stage stage = mapper.mapStageRequestToStage(stageRequest);
            stageService.saveStage(gameSaveId, stage, redisCacheService.isEnabled());

            return generateResponse(HttpStatus.OK);
        } catch (UnauthorizedException e) {
            log.error("Unauthorized exception while updating stages: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (NotFoundException e) {
            log.error("Game Save with id {} not found", gameSaveId);
            return generateResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error while updating stages: ", e);
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
