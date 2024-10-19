package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.StageEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.common.Filter;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserRefreshLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserUpdateRequest;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for BDD tests
 */
@UtilityClass
public class BddUtils {

    private static final String COMMA = ",";

    /**
     * Maps a row from a BDD table to a CurrencyRequest
     *
     * @param row row from BDD table
     * @return CurrencyRequest
     */
    public static CurrencyRequest mapToCurrencyRequest(Map<String, String> row) {
        String gold = row.get(BddFieldConstants.Currency.GOLD);
        String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
        String emerald = row.get(BddFieldConstants.Currency.EMERALD);
        String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);

        long goldLong = gold == null ? 0 : Long.parseLong(gold);
        long diamondLong = diamond == null ? 0 : Long.parseLong(diamond);
        long emeraldLong = emerald == null ? 0 : Long.parseLong(emerald);
        long amethystLong = amethyst == null ? 0 : Long.parseLong(amethyst);

        return new CurrencyRequest(goldLong, diamondLong, emeraldLong, amethystLong);
    }

    /**
     * Maps a row from a BDD table to a StageRequest
     *
     * @param row row from BDD table
     * @return StageRequest
     */
    public static StageRequest mapToStageRequest(Map<String, String> row) {
        String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
        String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);

        long currentStageLong = Long.parseLong(currentStage);
        long maxStageLong = Long.parseLong(maxStage);

        return new StageRequest(currentStageLong, maxStageLong);
    }


    /**
     * Maps a row from a BDD table to a GameSaveEntity
     *
     * @param row            row from BDD table
     * @return GameSaveEntity
     */
    public static GameSaveEntity mapToGameSaveEntity(Map<String, String> row) {
        String id = row.get(BddFieldConstants.GameSave.ID);
        String userId = row.get(BddFieldConstants.GameSave.USER_ID);
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);
        String gold = row.get(BddFieldConstants.GameSave.GOLD);
        String diamond = row.get(BddFieldConstants.GameSave.DIAMOND);
        String emerald = row.get(BddFieldConstants.GameSave.EMERALD);
        String amethyst = row.get(BddFieldConstants.GameSave.AMETHYST);
        String healthPoints = row.get(BddFieldConstants.GameSave.HEALTH_POINTS);
        String attack = row.get(BddFieldConstants.GameSave.ATTACK);
        String currentStageString = row.get(BddFieldConstants.GameSave.CURRENT_STAGE);
        String maxStageString = row.get(BddFieldConstants.GameSave.MAX_STAGE);

        long attackLong = Long.parseLong(attack);
        long healthPointsLong = Long.parseLong(healthPoints);
        long goldLong = Long.parseLong(gold);
        long diamondLong = Long.parseLong(diamond);
        long emeraldLong = Long.parseLong(emerald);
        long amethystLong = Long.parseLong(amethyst);
        long currentStage = Long.parseLong(currentStageString);
        long maxStage = Long.parseLong(maxStageString);

        GameSaveEntity gameSaveEntity = GameSaveEntity.builder()
                .userEmail(userEmail)
                .nickname(nickname)
                .id(id)
                .attack(attackLong)
                .healthPoints(healthPointsLong)
                .build();

        CurrencyEntity currencyEntity = CurrencyEntity.builder()
                .id(id)
                .gameSave(gameSaveEntity)
                .goldAmount(goldLong)
                .diamondAmount(diamondLong)
                .emeraldAmount(emeraldLong)
                .amethystAmount(amethystLong)
                .userEmail(userEmail)
                .build();

        gameSaveEntity.setCurrencyEntity(currencyEntity);

        StageEntity stageEntity = StageEntity.builder()
                .id(id)
                .currentStage(currentStage)
                .maxStage(maxStage)
                .gameSave(gameSaveEntity)
                .build();

        gameSaveEntity.setStageEntity(stageEntity);

        return gameSaveEntity;
    }

    /**
     * Maps a row from a BDD table to a UserInfo
     *
     * @param row row from BDD table
     * @return UserInfo
     */
    public static UserInfo mapToUserInfo(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserInfo.EMAIL);
        String name = row.get(BddFieldConstants.UserInfo.NAME);
        String rolesString = row.get(BddFieldConstants.UserInfo.ROLES);
        String verifiedString = row.get(BddFieldConstants.UserInfo.VERIFIED);
        Set<UserRole> roles = Collections.emptySet();
        if (rolesString != null) {
            roles = Arrays.stream(rolesString.split(COMMA)).map(UserRole::valueOf).collect(Collectors.toSet());
        }

        boolean verified = Boolean.parseBoolean(verifiedString);
        return null;
        //return new UserInfo(name, email, verified, roles, null, null);
    }


    /**
     * Initializes the RestTemplate inside TestRestTemplate
     *
     * @param testRestTemplate the TestRestTemplate
     */
    public void initTestRestTemplate(TestRestTemplate testRestTemplate) {
        RestTemplate template = testRestTemplate.getRestTemplate();
        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        template.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus status = HttpStatus.resolve(response.getStatusCode().value());
                return status.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });
    }

    /**
     * Maps a row from a BDD table to a User
     *
     * @param row row from BDD table
     * @return User
     * @throws NotFoundException if user is not found
     */
    public static User mapToUser(Map<String, String> row) throws NotFoundException {
        String id = row.get(BddFieldConstants.User.ID);
        String email = row.get(BddFieldConstants.User.EMAIL);
        String firstName = row.get(BddFieldConstants.User.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.User.LAST_NAME);
        String provider = row.get(BddFieldConstants.User.PROVIDER);
        String roles = row.get(BddFieldConstants.User.ROLES);
        return User.builder()
                .userRoles(Arrays.stream(roles.split(",")).toList())
                .id(id)
                .username(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    /**
     * Maps a row from a BDD table to an AdminGameSaveCreationRequest
     *
     * @param row row from BDD table
     * @return AdminGameSaveCreationRequest
     */
    public static AdminGameSaveCreationRequest mapToAdminGameSaveCreationRequest(Map<String, String> row) {
        String goldString = row.get(BddFieldConstants.GameSave.GOLD);
        String healthPointsString = row.get(BddFieldConstants.GameSave.HEALTH_POINTS);
        String attackString = row.get(BddFieldConstants.GameSave.ATTACK);
        String diamondString = row.get(BddFieldConstants.GameSave.DIAMOND);
        String emeraldString = row.get(BddFieldConstants.GameSave.EMERALD);
        String amethystString = row.get(BddFieldConstants.GameSave.AMETHYST);
        String currentStageString = row.get(BddFieldConstants.GameSave.CURRENT_STAGE);
        String maxStageString = row.get(BddFieldConstants.GameSave.MAX_STAGE);


        Long gold = Long.parseLong(goldString);
        Long healthPoints = Long.parseLong(healthPointsString);
        Long attack = Long.parseLong(attackString);
        Long diamond = Long.parseLong(diamondString);
        Long emerald = Long.parseLong(emeraldString);
        Long amethyst = Long.parseLong(amethystString);
        Long currentStage = Long.parseLong(currentStageString);
        Long maxStage = Long.parseLong(maxStageString);

        String id = row.get(BddFieldConstants.GameSave.ID);
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);

        return AdminGameSaveCreationRequest.builder()
                .diamond(diamond)
                .gold(gold)
                .emerald(emerald)
                .amethyst(amethyst)
                .healthPoints(healthPoints)
                .attack(attack)
                .id(id)
                .maxStage(maxStage)
                .currentStage(currentStage)
                .userEmail(userEmail)
                .build();
    }

    /**
     * Maps a row from a BDD table to a GameSave
     *
     * @param row row from BDD table
     * @return GameSave
     */
    public static GameSave mapToGameSave(Map<String, String> row) {
        String id = row.get(BddFieldConstants.GameSave.ID);
        String userId = row.get(BddFieldConstants.GameSave.USER_ID);
        String userEmail = row.get(BddFieldConstants.GameSave.USER_EMAIL);
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        long healthPoints = Long.parseLong(row.get(BddFieldConstants.GameSave.HEALTH_POINTS));
        long attack = Long.parseLong(row.get(BddFieldConstants.GameSave.ATTACK));

        Currency currency = mapToCurrency(row);
        Stage stage = mapToStage(row);

        return GameSave.builder()
                .attack(attack)
                .userEmail(userEmail)
                .nickname(nickname)
                .id(id)
                .healthPoints(healthPoints)
                .currency(currency)
                .stage(stage)
                .build();
    }

    /**
     * Maps a row from a BDD table to a Currency POJO
     *
     * @param row row from BDD table
     * @return Currency
     */
    public static Currency mapToCurrency(Map<String, String> row) {
        String gold = row.get(BddFieldConstants.Currency.GOLD);
        String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
        String emerald = row.get(BddFieldConstants.Currency.EMERALD);
        String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);

        Long goldLong = gold == null ? null : Long.parseLong(gold);
        Long diamondLong = diamond == null ? null : Long.parseLong(diamond);
        Long emeraldLong = emerald == null ? null : Long.parseLong(emerald);
        Long amethystLong = amethyst == null ? null : Long.parseLong(amethyst);

        return new Currency(goldLong, diamondLong, emeraldLong, amethystLong);
    }

    /**
     * Maps a row from a BDD table to a Stage
     *
     * @param row row from BDD table
     * @return Stage
     */
    public static Stage mapToStage(Map<String, String> row) {
        String currentStage = row.get(BddFieldConstants.Stage.CURRENT_STAGE);
        String maxStage = row.get(BddFieldConstants.Stage.MAX_STAGE);

        long currentStageLong = Long.parseLong(currentStage);
        long maxStageLong = Long.parseLong(maxStage);

        return Stage.builder()
                .currentStage(currentStageLong)
                .maxStage(maxStageLong)
                .build();
    }

    /**
     * Maps a row from a BDD table to a AdminGameSaveUpdateRequest
     *
     * @param row row from BDD table
     * @return AdminGameSaveUpdateRequest
     */
    public static AdminGameSaveUpdateRequest mapToAdminGameSaveUpdateRequest(Map<String, String> row) {
        String healthPoints = row.get(BddFieldConstants.GameSave.HEALTH_POINTS);
        String attack = row.get(BddFieldConstants.GameSave.ATTACK);
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        Long healthPointsLong = Long.parseLong(healthPoints);
        Long attackLong = Long.parseLong(attack);


        return AdminGameSaveUpdateRequest.builder()
                .healthPoints(healthPointsLong)
                .attack(attackLong)
                .nickname(nickname)
                .build();
    }

    /**
     * Maps a row from a BDD table to a GameSaveUpdateUserRequest
     *
     * @param row row from BDD table
     * @return GameSaveUpdateRequest
     */
    public static GameSaveUpdateNicknameRequest mapToGameSaveUpdateUserRequest(Map<String, String> row) {
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        return new GameSaveUpdateNicknameRequest(nickname);
    }

    /**
     * Maps a row from a BDD table to a UserEntity
     *
     * @param row row from BDD table
     * @return UserEntity
     */
    public static UserEntity mapToUserEntity(Map<String, String> row) {
        String email = row.get(BddFieldConstants.User.EMAIL);
        String firstName = row.get(BddFieldConstants.User.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.User.LAST_NAME);
        String id = row.get(BddFieldConstants.User.ID);
        String password = row.get(BddFieldConstants.User.PASSWORD);
        String provider = row.get(BddFieldConstants.User.PROVIDER);
        String enabled = row.get(BddFieldConstants.User.ENABLED);
        String verified = row.get(BddFieldConstants.User.VERIFIED);
        String roles = row.get(BddFieldConstants.User.ROLES);


        Boolean enabledBoolean = Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = Boolean.parseBoolean(verified);
        SocialProvider socialProvider = SocialProvider.fromString(provider);
        Set<UserRole> roleSet = Arrays.stream(roles.split(COMMA))
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());


        return UserEntity.builder()
                .email(email)
                .id(id)
                .verified(verifiedBoolean)
                .enabled(enabledBoolean)
                .password(password)
                .provider(socialProvider)
                .build();
    }

    /**
     * Maps a row from a BDD table to a UserCreationRequest
     *
     * @param row row from BDD table
     * @return UserCreationRequest
     */
    public static UserCreationRequest mapToUserCreationRequest(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserCreationRequest.EMAIL);
        String firstName = row.get(BddFieldConstants.UserCreationRequest.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.UserCreationRequest.LAST_NAME);
        String password = row.get(BddFieldConstants.UserCreationRequest.PASSWORD);

        return UserCreationRequest.builder()
                .username(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .build();
    }

    /**
     * Builds a URL from the server port and the endpoint to call
     *
     * @param port     port
     * @param endpoint endpoint
     * @return the URL
     */
    public static String buildUrl(int port, String endpoint) {
        return "http://localhost:" + port + endpoint;
    }

    /**
     * Builds an HttpEntity with the given body and headers
     *
     * @param body body
     * @param <T>  type of the body
     * @return HttpEntity
     */
    public static <T> HttpEntity<T> buildHttpEntity(T body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        return new HttpEntity<>(body, httpHeaders);
    }

    /**
     * Maps a row from a BDD table to a UserLoginRequest
     *
     * @param row row from BDD table
     * @return UserLoginRequest
     */
    public static UserLoginRequest mapToUserLoginRequest(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserLoginRequest.EMAIL);
        String password = row.get(BddFieldConstants.UserLoginRequest.PASSWORD);

        return new UserLoginRequest(email, password);
    }

    /**
     * Maps a row from a BDD table to a JwtAuthentication
     *
     * @param row row from BDD table
     * @return JwtAuthentication
     */
    public static UserUpdateRequest mapToUserUpdateRequest(Map<String, String> row) {
        String firstName = row.get(BddFieldConstants.UserUpdateRequest.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.UserUpdateRequest.LAST_NAME);

        return new UserUpdateRequest(firstName, lastName);
    }

    /**
     * Maps a row from a BDD table to a AdminUserUpdateRequest
     *
     * @param row row from BDD table
     * @return AdminUserUpdateRequest
     */
    public static AdminUserUpdateRequest mapToAdminUserUpdateRequest(Map<String, String> row) {
        String firstName = row.get(BddFieldConstants.AdminUserUpdateRequest.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.AdminUserUpdateRequest.LAST_NAME);
        String enabled = row.get(BddFieldConstants.AdminUserUpdateRequest.ENABLED);
        String verified = row.get(BddFieldConstants.AdminUserUpdateRequest.VERIFIED);
        String userRoles = row.get(BddFieldConstants.AdminUserUpdateRequest.USER_ROLES);

        List<String> roles = Arrays.stream(userRoles.split(COMMA)).toList();

        Boolean enabledBoolean = Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = Boolean.parseBoolean(verified);

        return new AdminUserUpdateRequest(firstName, lastName, verifiedBoolean, enabledBoolean, roles);
    }

    /**
     * Maps a row from a BDD table to a AdminUserCreationRequest
     *
     * @param row row from BDD table
     * @return AdminUserCreationRequest
     */
    public static AdminUserCreationRequest mapToAdminUserCreationRequest(Map<String, String> row) {
        String firstName = row.get(BddFieldConstants.AdminUserCreationRequest.FIRST_NAME);
        String lastName = row.get(BddFieldConstants.AdminUserCreationRequest.LAST_NAME);
        String email = row.get(BddFieldConstants.AdminUserCreationRequest.EMAIL);
        String enabled = row.get(BddFieldConstants.AdminUserCreationRequest.ENABLED);
        String userId = row.get(BddFieldConstants.AdminUserCreationRequest.USER_ID);
        String verified = row.get(BddFieldConstants.AdminUserCreationRequest.VERIFIED);

        Boolean enabledBoolean = Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = Boolean.parseBoolean(verified);

        return new AdminUserCreationRequest(firstName, lastName, userId, enabledBoolean, verifiedBoolean, email);

    }

    /**
     * Maps a row from a BDD table to a SearchRequest Filter
     *
     * @param row row from BDD table
     * @return Filter
     */
    public static Filter mapToFilter(Map<String, String> row) {
        String key = row.get(BddFieldConstants.SearchRequest.KEY);
        String value = row.get(BddFieldConstants.SearchRequest.VALUE);
        return new Filter(key, value);
    }

    /**
     * Maps a row from a BDD table to a GlobalInfo
     *
     * @param row row from BDD table
     * @return GlobalInfo
     */
    public static GlobalInfo mapToGlobalInfo(Map<String, String> row) {
        String nbGameSaves = row.get(BddFieldConstants.GlobalInfo.GAME_SAVE_COUNTER);
        String nbUsers = row.get(BddFieldConstants.GlobalInfo.USER_COUNTER);
        String now = row.get(BddFieldConstants.GlobalInfo.NOW);

        Long nbGameSavesLong = Long.parseLong(nbGameSaves);
        Long nbUsersLong = Long.parseLong(nbUsers);

        Instant nowInstant = Instant.parse(now);

        return new GlobalInfo(nowInstant, nbGameSavesLong, nbUsersLong);
    }

    /**
     * Maps a row from a BDD table to a UserRefreshLoginRequest
     *
     * @param row row from BDD table
     * @return UserRefreshLoginRequest
     */
    public static UserRefreshLoginRequest mapToUserRefreshLoginRequest(Map<String, String> row) {
        String refreshToken = row.get(BddFieldConstants.UserRefreshLoginRequest.REFRESH_TOKEN);
        return new UserRefreshLoginRequest(refreshToken);
    }
}
