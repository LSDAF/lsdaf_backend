package com.lsadf.lsadf_backend.utils;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.constants.SocialProvider;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.entities.*;
import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.tokens.UserVerificationTokenEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.common.Filter;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;
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
     * Maps a row from a BDD table to a RefreshTokenEntity
     *
     * @param row            row from BDD table
     * @param userRepository UserRepository
     * @return RefreshTokenEntity
     */
    public static RefreshTokenEntity mapToRefreshTokenEntity(Map<String, String> row, UserRepository userRepository) {
        String token = row.get(BddFieldConstants.RefreshToken.REFRESH_TOKEN);
        String expirationDate = row.get(BddFieldConstants.RefreshToken.EXPIRATION_DATE);
        String invalidationDate = row.get(BddFieldConstants.RefreshToken.INVALIDATION_DATE);
        String status = row.get(BddFieldConstants.RefreshToken.STATUS);
        String userEmail = row.get(BddFieldConstants.RefreshToken.USER_EMAIL);

        Date expirationDateDate = null;
        Date invalidationDateDate = null;

        if (expirationDate != null) {
            expirationDateDate = DateUtils.stringToDate(expirationDate);
        }
        if (invalidationDate != null) {
            invalidationDateDate = DateUtils.stringToDate(invalidationDate);
        }
        TokenStatus statusEnum = TokenStatus.valueOf(status);

        Optional<UserEntity> userEntityOptional = userRepository.findUserEntityByEmail(userEmail);
        UserEntity userEntity = userEntityOptional.orElse(null);

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setToken(token);
        entity.setExpirationDate(expirationDateDate);
        entity.setInvalidationDate(invalidationDateDate);
        entity.setStatus(statusEnum);
        entity.setUser(userEntity);

        return entity;
    }

    /**
     * Maps a row from a BDD table to a GameSaveEntity
     *
     * @param row            row from BDD table
     * @param userRepository UserRepository
     * @return GameSaveEntity
     */
    public static GameSaveEntity mapToGameSaveEntity(Map<String, String> row, UserRepository userRepository) {
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

        long attackLong = attack == null ? 0 : Long.parseLong(attack);
        long healthPointsLong = healthPoints == null ? 0 : Long.parseLong(healthPoints);
        long goldLong = gold == null ? 0 : Long.parseLong(gold);
        long diamondLong = diamond == null ? 0 : Long.parseLong(diamond);
        long emeraldLong = emerald == null ? 0 : Long.parseLong(emerald);
        long amethystLong = amethyst == null ? 0 : Long.parseLong(amethyst);

        if (userId == null) {
            userId = "";
        }
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findUserEntityByEmail(userEmail);
        }

        GameSaveEntity gameSaveEntity = GameSaveEntity.builder()
                .user(userOptional.orElse(null))
                .nickname(nickname)
                .attack(attackLong)
                .healthPoints(healthPointsLong)
                .build();

        gameSaveEntity.setId(id);

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

        return gameSaveEntity;
    }

    /**
     * Maps a row from a BDD table to a UserInfo
     *
     * @param row row from BDD table
     * @return UserInfo
     */
    public static UserInfo mapToUserInfo(Map<String, String> row) {
        String id = row.get(BddFieldConstants.UserInfo.ID);
        String email = row.get(BddFieldConstants.UserInfo.EMAIL);
        String name = row.get(BddFieldConstants.UserInfo.NAME);
        String rolesString = row.get(BddFieldConstants.UserInfo.ROLES);
        String verifiedString = row.get(BddFieldConstants.UserInfo.VERIFIED);
        Set<UserRole> roles = Collections.emptySet();
        if (rolesString != null) {
            roles = Arrays.stream(rolesString.split(COMMA)).map(UserRole::valueOf).collect(Collectors.toSet());
        }

        boolean verified = Boolean.parseBoolean(verifiedString);

        return new UserInfo(id, name, email, verified, roles, null, null);
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
     * @param row            row from BDD table
     * @param userRepository UserRepository
     * @return User
     * @throws NotFoundException if user is not found
     */
    public static User mapToUser(Map<String, String> row, UserRepository userRepository) throws NotFoundException {
        String id = row.get(BddFieldConstants.User.ID);
        String email = row.get(BddFieldConstants.User.EMAIL);
        String name = row.get(BddFieldConstants.User.NAME);
        String provider = row.get(BddFieldConstants.User.PROVIDER);
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(NotFoundException::new);

        return User.builder()
                .userRoles(userEntity.getRoles().stream().toList())
                .password(userEntity.getPassword())
                .socialProvider(SocialProvider.fromString(provider))
                .id(id)
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .email(email)
                .name(name)
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


        Long gold = goldString != null ? Long.parseLong(goldString) : null;
        Long healthPoints = healthPointsString != null ? Long.parseLong(healthPointsString) : null;
        Long attack = attackString != null ? Long.parseLong(attackString) : null;
        Long diamond = diamondString != null ? Long.parseLong(diamondString) : null;
        Long emerald = emeraldString != null ? Long.parseLong(emeraldString) : null;
        Long amethyst = amethystString != null ? Long.parseLong(amethystString) : null;

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
        String goldString = row.get(BddFieldConstants.GameSave.GOLD);
        String diamondString = row.get(BddFieldConstants.GameSave.DIAMOND);
        String emeraldString = row.get(BddFieldConstants.GameSave.EMERALD);
        String amethystString = row.get(BddFieldConstants.GameSave.AMETHYST);
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        Long gold = goldString != null ? Long.parseLong(goldString) : null;
        Long diamond = diamondString != null ? Long.parseLong(diamondString) : null;
        Long emerald = emeraldString != null ? Long.parseLong(emeraldString) : null;
        Long amethyst = amethystString != null ? Long.parseLong(amethystString) : null;

        long healthPoints = Long.parseLong(row.get(BddFieldConstants.GameSave.HEALTH_POINTS));
        long attack = Long.parseLong(row.get(BddFieldConstants.GameSave.ATTACK));

        return GameSave.builder()
                .attack(attack)
                .userId(userId)
                .userEmail(userEmail)
                .nickname(nickname)
                .id(id)
                .healthPoints(healthPoints)
                .gold(gold)
                .diamond(diamond)
                .emerald(emerald)
                .amethyst(amethyst)
                .build();
    }

    /**
     * Maps a row from a BDD table to a Currency POJO
     *
     * @param row row from BDD table
     * @return Currency
     */
    public static com.lsadf.lsadf_backend.models.Currency mapToCurrency(Map<String, String> row) {
        String gold = row.get(BddFieldConstants.Currency.GOLD);
        String diamond = row.get(BddFieldConstants.Currency.DIAMOND);
        String emerald = row.get(BddFieldConstants.Currency.EMERALD);
        String amethyst = row.get(BddFieldConstants.Currency.AMETHYST);

        long goldLong = gold == null ? 0 : Long.parseLong(gold);
        long diamondLong = diamond == null ? 0 : Long.parseLong(diamond);
        long emeraldLong = emerald == null ? 0 : Long.parseLong(emerald);
        long amethystLong = amethyst == null ? 0 : Long.parseLong(amethyst);

        return new Currency(goldLong, diamondLong, emeraldLong, amethystLong);
    }

    /**
     * Maps a row from a BDD table to a AdminGameSaveUpdateRequest
     *
     * @param row row from BDD table
     * @return AdminGameSaveUpdateRequest
     */
    public static AdminGameSaveUpdateRequest mapToAdminGameSaveUpdateRequest(Map<String, String> row) {
        String gold = row.get(BddFieldConstants.GameSave.GOLD);
        String diamond = row.get(BddFieldConstants.GameSave.DIAMOND);
        String emerald = row.get(BddFieldConstants.GameSave.EMERALD);
        String amethyst = row.get(BddFieldConstants.GameSave.AMETHYST);
        String healthPoints = row.get(BddFieldConstants.GameSave.HEALTH_POINTS);
        String attack = row.get(BddFieldConstants.GameSave.ATTACK);
        String nickname = row.get(BddFieldConstants.GameSave.NICKNAME);

        Long goldLong = gold == null ? null : Long.parseLong(gold);
        Long diamondLong = diamond == null ? null : Long.parseLong(diamond);
        Long emeraldLong = emerald == null ? null : Long.parseLong(emerald);
        Long amethystLong = amethyst == null ? null : Long.parseLong(amethyst);
        Long healthPointsLong = healthPoints == null ? null : Long.parseLong(healthPoints);
        Long attackLong = attack == null ? null : Long.parseLong(attack);


        return AdminGameSaveUpdateRequest.builder()
                .diamond(diamondLong)
                .gold(goldLong)
                .emerald(emeraldLong)
                .amethyst(amethystLong)
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
        String name = row.get(BddFieldConstants.User.NAME);
        String id = row.get(BddFieldConstants.User.ID);
        String password = row.get(BddFieldConstants.User.PASSWORD);
        String provider = row.get(BddFieldConstants.User.PROVIDER);
        String enabled = row.get(BddFieldConstants.User.ENABLED);
        String verified = row.get(BddFieldConstants.User.VERIFIED);
        String roles = row.get(BddFieldConstants.User.ROLES);


        Boolean enabledBoolean = enabled == null ? null : Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = verified == null ? null : Boolean.parseBoolean(verified);
        SocialProvider socialProvider = SocialProvider.fromString(provider);
        Set<UserRole> roleSet = roles == null
                ? Set.of(UserRole.USER)
                : Arrays.stream(roles.split(COMMA))
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());


        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .name(name)
                .verified(verifiedBoolean)
                .enabled(enabledBoolean)
                .password(password)
                .provider(socialProvider)
                .roles(roleSet)
                .build();

        userEntity.setId(id);

        return userEntity;
    }

    /**
     * Maps a row from a BDD table to a UserCreationRequest
     *
     * @param row row from BDD table
     * @return UserCreationRequest
     */
    public static UserCreationRequest mapToUserCreationRequest(Map<String, String> row) {
        String email = row.get(BddFieldConstants.UserCreationRequest.EMAIL);
        String name = row.get(BddFieldConstants.UserCreationRequest.NAME);
        String password = row.get(BddFieldConstants.UserCreationRequest.PASSWORD);

        return UserCreationRequest.builder()
                .email(email)
                .name(name)
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
        String name = row.get(BddFieldConstants.UserUpdateRequest.NAME);

        return new UserUpdateRequest(name);
    }

    /**
     * Maps a row from a BDD table to a AdminUserUpdateRequest
     *
     * @param row row from BDD table
     * @return AdminUserUpdateRequest
     */
    public static AdminUserUpdateRequest mapToAdminUserUpdateRequest(Map<String, String> row) {
        String name = row.get(BddFieldConstants.AdminUserUpdateRequest.NAME);
        String password = row.get(BddFieldConstants.AdminUserUpdateRequest.PASSWORD);
        String email = row.get(BddFieldConstants.AdminUserUpdateRequest.EMAIL);
        String enabled = row.get(BddFieldConstants.AdminUserUpdateRequest.ENABLED);
        String verified = row.get(BddFieldConstants.AdminUserUpdateRequest.VERIFIED);
        String userRoles = row.get(BddFieldConstants.AdminUserUpdateRequest.USER_ROLES);

        Set<UserRole> roles = null;
        if (userRoles != null) {
            roles = Arrays.stream(userRoles.split(COMMA)).map(UserRole::valueOf).collect(Collectors.toSet());
        }
        Boolean enabledBoolean = enabled == null ? null : Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = verified == null ? null : Boolean.parseBoolean(verified);

        return new AdminUserUpdateRequest(name, password, verifiedBoolean, email, enabledBoolean, roles);
    }

    /**
     * Maps a row from a BDD table to a AdminUserCreationRequest
     *
     * @param row row from BDD table
     * @return AdminUserCreationRequest
     */
    public static AdminUserCreationRequest mapToAdminUserCreationRequest(Map<String, String> row) {
        String name = row.get(BddFieldConstants.AdminUserCreationRequest.NAME);
        String email = row.get(BddFieldConstants.AdminUserCreationRequest.EMAIL);
        String enabled = row.get(BddFieldConstants.AdminUserCreationRequest.ENABLED);
        String userId = row.get(BddFieldConstants.AdminUserCreationRequest.USER_ID);
        String password = row.get(BddFieldConstants.AdminUserCreationRequest.PASSWORD);
        String provider = row.get(BddFieldConstants.AdminUserCreationRequest.SOCIAL_PROVIDER);
        String verified = row.get(BddFieldConstants.AdminUserCreationRequest.VERIFIED);
        String providerUserId = row.get(BddFieldConstants.AdminUserCreationRequest.PROVIDER_USER_ID);
        String userRoles = row.get(BddFieldConstants.AdminUserCreationRequest.ROLES);

        List<UserRole> roles = null;

        if (userRoles != null) {
            roles = Arrays.stream(userRoles.split(COMMA)).map(UserRole::valueOf).collect(Collectors.toList());
        }
        SocialProvider socialProvider = SocialProvider.fromString(provider);

        Boolean enabledBoolean = enabled == null ? null : Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = verified == null ? null : Boolean.parseBoolean(verified);

        return new AdminUserCreationRequest(name, userId, enabledBoolean, verifiedBoolean, email, password, socialProvider, roles, providerUserId);

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

        Long nbGameSavesLong = nbGameSaves == null ? 0L : Long.parseLong(nbGameSaves);
        Long nbUsersLong = nbUsers == null ? 0L : Long.parseLong(nbUsers);

        Instant nowInstant = now == null ? null : Instant.parse(now);

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
        String email = row.get(BddFieldConstants.UserRefreshLoginRequest.EMAIL);
        return new UserRefreshLoginRequest(email, refreshToken);
    }

    /**
     * Maps a row from a BDD table to a UserAdminDetails
     *
     * @param row row from BDD table
     * @return UserAdminDetails
     */
    public static UserAdminDetails mapToUserAdminDetails(Map<String, String> row) {
        String id = row.get(BddFieldConstants.UserAdminDetails.ID);
        String email = row.get(BddFieldConstants.UserAdminDetails.EMAIL);
        String name = row.get(BddFieldConstants.UserAdminDetails.NAME);
        String password = row.get(BddFieldConstants.UserAdminDetails.PASSWORD);
        String provider = row.get(BddFieldConstants.UserAdminDetails.SOCIAL_PROVIDER);
        String enabled = row.get(BddFieldConstants.UserAdminDetails.ENABLED);
        String verified = row.get(BddFieldConstants.UserAdminDetails.VERIFIED);
        String roles = row.get(BddFieldConstants.UserAdminDetails.ROLES);

        Boolean enabledBoolean = enabled == null ? null : Boolean.parseBoolean(enabled);
        Boolean verifiedBoolean = verified == null ? null : Boolean.parseBoolean(verified);
        SocialProvider socialProvider = SocialProvider.fromString(provider);
        Set<UserRole> roleSet = roles == null
                ? Set.of(UserRole.USER)
                : Arrays.stream(roles.split(COMMA))
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());


        return UserAdminDetails.builder()
                .id(id)
                .email(email)
                .name(name)
                .verified(verifiedBoolean)
                .password(password)
                .socialProvider(socialProvider)
                .enabled(enabledBoolean)
                .userRoles(roleSet.stream().toList())
                .build();
    }

    /**
     * Maps a row from a BDD table to a UserVerificationTokenEntity
     *
     * @param row row from BDD table
     * @return UserVerificationTokenEntity
     */
    public static UserVerificationTokenEntity mapToUserVerificationTokenEntity(Map<String, String> row,
                                                                               UserRepository userRepository) {
        String token = row.get(BddFieldConstants.UserVerificationToken.TOKEN);
        String userEmail = row.get(BddFieldConstants.UserVerificationToken.USER_EMAIL);
        String status = row.get(BddFieldConstants.UserVerificationToken.STATUS);
        String expirationDate = row.get(BddFieldConstants.UserVerificationToken.EXPIRATION_DATE);
        Date expirationDateDate = null;

        UserEntity user = userRepository.findUserEntityByEmail(userEmail).orElse(null);

        if (expirationDate != null) {
            expirationDateDate = DateUtils.stringToDate(expirationDate);
        }

        TokenStatus tokenStatus = TokenStatus.valueOf(status);

        UserVerificationTokenEntity entity = new UserVerificationTokenEntity();
        entity.setToken(token);
        entity.setUser(user);
        entity.setExpirationDate(expirationDateDate);
        entity.setStatus(tokenStatus);

        return entity;
    }
}
