package com.lsadf.lsadf_backend.controllers.impl;

import com.lsadf.lsadf_backend.controllers.AdminController;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.AlreadyTakenNicknameException;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.exceptions.http.UnauthorizedException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

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
    private final GameSaveService gameSaveService;
    private final SearchService searchService;
    private final CacheService localCacheService;
    private final CacheFlushService cacheFlushService;
    private final ClockService clockService;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    @SuppressWarnings("java:S107")
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
     */
    @Override
    public ResponseEntity<GenericResponse<GlobalInfo>> getGlobalInfo(@AuthenticationPrincipal Jwt jwt) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(@AuthenticationPrincipal Jwt jwt) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<Boolean>> toggleRedisCacheEnabling(@AuthenticationPrincipal Jwt jwt) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<List<User>>> getUsers(@AuthenticationPrincipal Jwt jwt,
                                                                @RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        try {
            return generateResponse(HttpStatus.OK);
        } catch (
                UnauthorizedException e) {
            log.error("Unauthorized exception while getting users: ", e);
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (
                Exception e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> getDetailedUserById(@AuthenticationPrincipal Jwt jwt,
                                                                     @PathVariable(value = USER_ID) String userId) {
        try {
            validateUser(jwt);
            User user = userService.getUserById(userId);
            return generateResponse(HttpStatus.OK, user);
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
    public ResponseEntity<GenericResponse<User>> getDetailedUserByEmail(@AuthenticationPrincipal Jwt jwt,
                                                                        @PathVariable(value = USER_EMAIL) String userEmail) {
        try {
            validateUser(jwt);
            User user = userService.getUserByUsername(userEmail);
            return generateResponse(HttpStatus.OK, user);
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
    public ResponseEntity<GenericResponse<User>> updateUser(@AuthenticationPrincipal Jwt jwt,
                                                            @PathVariable(value = USER_ID) String userId,
                                                            @Valid @RequestBody AdminUserUpdateRequest user) {
        try {
            validateUser(jwt);
            userService.updateUser(userId, user);
            return generateResponse(HttpStatus.OK, null);
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
    public ResponseEntity<GenericResponse<Void>> deleteUser(@AuthenticationPrincipal Jwt jwt,
                                                            @PathVariable(value = USER_ID) String userId) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<User>> createUser(@AuthenticationPrincipal Jwt jwt,
                                                            @Valid @RequestBody AdminUserCreationRequest adminUserCreationRequest) {
        try {
            validateUser(jwt);
            UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                    .username(adminUserCreationRequest.getUsername())
                    .firstName(adminUserCreationRequest.getFirstName())
                    .lastName(adminUserCreationRequest.getLastName())
                    .emailVerified(adminUserCreationRequest.getEmailVerified())
                    .enabled(adminUserCreationRequest.getEnabled())
                    .build();
            User user = userService.createUser(userCreationRequest);
            user.setEnabled(adminUserCreationRequest.getEnabled());
            user.setEmailVerified(adminUserCreationRequest.getEmailVerified());

            return generateResponse(HttpStatus.OK, null);
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
    public ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(@AuthenticationPrincipal Jwt jwt,
                                                                        @RequestParam(value = ORDER_BY, required = false) GameSaveOrderBy orderBy) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<GameSave>> getGameSave(@AuthenticationPrincipal Jwt jwt,
                                                                 @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<GameSave>> updateGameSave(@AuthenticationPrincipal Jwt jwt,
                                                                    @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                    @Valid @RequestBody AdminGameSaveUpdateRequest adminGameSaveUpdateRequest) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@AuthenticationPrincipal Jwt jwt,
                                                                         @Valid @RequestBody AdminGameSaveCreationRequest creationRequest) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<User>> searchUsers(@AuthenticationPrincipal Jwt jwt,
                                                             @Valid @RequestBody SearchRequest searchRequest,
                                                             @RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        try {
            validateUser(jwt);
            searchService.searchUsers(searchRequest, orderBy);
            return generateResponse(HttpStatus.OK);
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
    public ResponseEntity<GenericResponse<GameSave>> searchGameSaves(@AuthenticationPrincipal Jwt jwt,
                                                                     @Valid @RequestBody SearchRequest searchRequest,
                                                                     @RequestParam(value = ORDER_BY) GameSaveOrderBy orderBy) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<Void>> deleteGameSave(@AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        try {
            validateUser(jwt);

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
    public ResponseEntity<GenericResponse<Void>> flushAndClearCache(@AuthenticationPrincipal Jwt jwt) {
        try {
            validateUser(jwt);

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
    public ResponseEntity<GenericResponse<Void>> updateGameSaveCurrency(@AuthenticationPrincipal Jwt jwt,
                                                                        @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                        @Valid @RequestBody CurrencyRequest currencyRequest) {
        try {
            validateUser(jwt);
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
    public ResponseEntity<GenericResponse<Void>> updateGameSaveStages(Jwt jwt,
                                                                      @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                      @Valid @RequestBody StageRequest stageRequest) {
        try {
            validateUser(jwt);
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
