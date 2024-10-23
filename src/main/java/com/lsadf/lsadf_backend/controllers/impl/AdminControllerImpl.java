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
                               ClockService clockService) {
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Boolean>> isCacheEnabled(@AuthenticationPrincipal Jwt jwt) {
        validateUser(jwt);
        boolean cacheEnabled = redisCacheService.isEnabled();
        return generateResponse(HttpStatus.OK, cacheEnabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Boolean>> toggleRedisCacheEnabling(@AuthenticationPrincipal Jwt jwt) {
        validateUser(jwt);
        redisCacheService.toggleCacheEnabling();
        Boolean cacheEnabled = redisCacheService.isEnabled();
        return generateResponse(HttpStatus.OK, cacheEnabled);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<User>>> getUsers(@AuthenticationPrincipal Jwt jwt,
                                                                @RequestParam(value = ORDER_BY) UserOrderBy orderBy) {
        var users = userService.getUsers()
                .toList();
        return generateResponse(HttpStatus.OK, users);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> getDetailedUserById(@AuthenticationPrincipal Jwt jwt,
                                                                     @PathVariable(value = USER_ID) String userId) {
        validateUser(jwt);
        User user = userService.getUserById(userId);
        return generateResponse(HttpStatus.OK, user);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> getDetailedUserByEmail(@AuthenticationPrincipal Jwt jwt,
                                                                        @PathVariable(value = USER_EMAIL) String userEmail) {
        validateUser(jwt);
        User user = userService.getUserByUsername(userEmail);
        return generateResponse(HttpStatus.OK, user);
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
        validateUser(jwt);
        userService.updateUser(userId, user);
        return generateResponse(HttpStatus.OK, null);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> deleteUser(@AuthenticationPrincipal Jwt jwt,
                                                            @PathVariable(value = USER_ID) String userId) {
        validateUser(jwt);
        userService.deleteUser(userId);
        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<User>> createUser(@AuthenticationPrincipal Jwt jwt,
                                                            @Valid @RequestBody AdminUserCreationRequest adminUserCreationRequest) {
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
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<List<GameSave>>> getSaveGames(@AuthenticationPrincipal Jwt jwt,
                                                                        @RequestParam(value = ORDER_BY, required = false) GameSaveOrderBy orderBy) {
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
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> getGameSave(@AuthenticationPrincipal Jwt jwt,
                                                                 @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {
        validateUser(jwt);
        GameSaveEntity entity = gameSaveService.getGameSave(gameSaveId);
        GameSave gameSave = mapper.mapToGameSave(entity);
        return generateResponse(HttpStatus.OK, gameSave);
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

        validateUser(jwt);
        GameSaveEntity gameSaveEntity = gameSaveService.updateNickname(gameSaveId, adminGameSaveUpdateRequest);
        GameSave gameSave = mapper.mapToGameSave(gameSaveEntity);
        return generateResponse(HttpStatus.OK, gameSave);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseEntity<GenericResponse<GameSave>> generateNewSaveGame(@AuthenticationPrincipal Jwt jwt,
                                                                         @Valid @RequestBody AdminGameSaveCreationRequest creationRequest) {

        validateUser(jwt);
        GameSaveEntity gameSaveEntity = gameSaveService.createGameSave(creationRequest);
        GameSave gameSave = mapper.mapToGameSave(gameSaveEntity);
        return generateResponse(HttpStatus.OK, gameSave);
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

        validateUser(jwt);
        searchService.searchUsers(searchRequest, orderBy);
        return generateResponse(HttpStatus.OK);
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
        validateUser(jwt);
        try (Stream<GameSaveEntity> gameSaveStream = searchService.searchGameSaves(searchRequest, orderBy)) {
            Stream<GameSaveEntity> sortedStream = StreamUtils.sortGameSaves(gameSaveStream, orderBy);
            List<GameSave> gameSaves = sortedStream
                    .map(mapper::mapToGameSave)
                    .toList();
            return generateResponse(HttpStatus.OK, gameSaves);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> deleteGameSave(@AuthenticationPrincipal Jwt jwt,
                                                                @PathVariable(value = GAME_SAVE_ID) String gameSaveId) {

        validateUser(jwt);

        gameSaveService.deleteGameSave(gameSaveId);
        return generateResponse(HttpStatus.OK);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> flushAndClearCache(@AuthenticationPrincipal Jwt jwt) {
        validateUser(jwt);

        log.info("Clearing all caches");
        cacheFlushService.flushCurrencies();
        cacheFlushService.flushStages();
        localCacheService.clearCaches();
        redisCacheService.clearCaches();

        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> updateGameSaveCurrency(@AuthenticationPrincipal Jwt jwt,
                                                                        @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                        @Valid @RequestBody CurrencyRequest currencyRequest) {

        validateUser(jwt);
        Currency currency = mapper.mapCurrencyRequestToCurrency(currencyRequest);
        currencyService.saveCurrency(gameSaveId, currency, redisCacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<GenericResponse<Void>> updateGameSaveStages(Jwt jwt,
                                                                      @PathVariable(value = GAME_SAVE_ID) String gameSaveId,
                                                                      @Valid @RequestBody StageRequest stageRequest) {

        validateUser(jwt);
        Stage stage = mapper.mapStageRequestToStage(stageRequest);
        stageService.saveStage(gameSaveId, stage, redisCacheService.isEnabled());

        return generateResponse(HttpStatus.OK);
    }
}
