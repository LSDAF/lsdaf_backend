package com.lsadf.lsadf_backend.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanConstants {


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Service {
        public static final String REDIS_CACHE_SERVICE = "redisCacheService";
        public static final String LOCAL_CACHE_SERVICE = "localCacheService";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class TokenProvider {
        public static final String JWT_TOKEN_PROVIDER = "jwtTokenProvider";
        public static final String REFRESH_TOKEN_PROVIDER = "refreshTokenProvider";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Cache {
        public static final String CHARACTERISTICS_CACHE = "characteristicsCache";
        public static final String CURRENCY_CACHE = "currencyCache";
        public static final String INVENTORY_CACHE = "inventoryCache";
        public static final String STAGE_CACHE = "stageCache";
        public static final String LOCAL_INVALIDATED_JWT_TOKEN_CACHE = "localInvalidatedJwtTokenCache";
        public static final String INVALIDATED_JWT_TOKEN_CACHE = "invalidatedJwtTokenCache";
        public static final String GAME_SAVE_OWNERSHIP_CACHE = "gameSaveOwnershipCache";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ClientRegistration {
        public static final String OAUTH2_GOOGLE_CLIENT_REGISTRATION = "oAuth2GoogleClientRegistration";
        public static final String OAUTH2_FACEBOOK_CLIENT_REGISTRATION = "oAuth2FacebookClientRegistration";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class TokenParser {
        public static final String JWT_TOKEN_PARSER = "jwtParser";
        public static final String JWT_REFRESH_TOKEN_PARSER = "jwtRefreshTokenParser";
    }
}
