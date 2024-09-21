package com.lsadf.lsadf_backend.bdd.given;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.utils.BddUtils;
import com.lsadf.lsadf_backend.utils.MockUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

/**
 * Step definitions for the given steps in the BDD scenarios
 */
@Slf4j(topic = "[GIVEN STEP DEFINITIONS]")
public class BddGivenStepDefinitions extends BddLoader {

    @Given("^the BDD engine is ready$")
    public void given_the_bdd_engine_is_ready() {
        this.currencyStack.clear();
        this.gameSaveListStack.clear();
        this.gameSaveEntityListStack.clear();
        this.exceptionStack.clear();
        this.gameSaveEntityListStack.clear();
        this.userListStack.clear();
        this.userEntityListStack.clear();
        this.globalInfoStack.clear();
        this.userInfoListStack.clear();
        this.localUserMap.clear();
        this.userAdminDetailsStack.clear();
        this.responseStack.clear();
        this.jwtTokenStack.clear();
        this.refreshJwtTokenStack.clear();
        this.booleanStack.clear();
        this.localUserMap.clear();

        this.localUserCache.clear();
        this.currencyCache.clear();
        this.gameSaveOwnershipCache.clear();
        this.invalidatedJwtTokenCache.clear();

        BddUtils.initTestRestTemplate(testRestTemplate);

        log.info("BDD engine is ready. Using port: {}", this.serverPort);
    }

    @Given("^the time clock set to the present$")
    public void given_the_time_clock_set_to_the_present() {
        log.info("Setting time clock to the present...");
        this.clockService.setClock(Clock.systemDefaultZone());
        log.info("Time clock set to the present");
    }

    @Given("^the time clock set to the following value (.*)$")
    public void given_the_time_clock_set_to_the_following_value(String time) {
        log.info("Setting time clock to the following value: {}", time);
        Instant instant = Instant.parse(time);
        ZoneId zoneId = clockService.getClock().getZone();
        clockService.setClock(Clock.fixed(instant, zoneId));
        log.info("Time clock set to the following value: {}", time);
    }

    @Given("^the cache is enabled$")
    public void given_the_cache_is_enabled() {
        log.info("Checking cache status...");
        boolean cacheEnabled = redisCacheService.isEnabled();
        if (!cacheEnabled) {
            log.info("Cache is disabled. Enabling cache...");
            redisCacheService.toggleCacheEnabling();
            assertThat(redisCacheService.isEnabled()).isTrue();
            log.info("Cache enabled");
        } else {
            log.info("Cache is already enabled");
        }
    }

    @Given("^the cache is disabled$")
    public void given_the_cache_is_disabled() {
        log.info("Checking cache status...");
        boolean cacheEnabled = redisCacheService.isEnabled();
        if (cacheEnabled) {
            log.info("Cache is enabled. Disabling cache...");
            redisCacheService.toggleCacheEnabling();
            assertThat(redisCacheService.isEnabled()).isFalse();
            log.info("Cache disabled");
        } else {
            log.info("Cache is already disabled");
        }
    }

    @Given("^the following refresh tokens$")
    public void given_the_following_refresh_tokens(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating refresh tokens...");

        rows.forEach(row -> {
            RefreshTokenEntity tokenToSave = BddUtils.mapToRefreshTokenEntity(row, userRepository);
            RefreshTokenEntity token = refreshTokenRepository.save(tokenToSave);
            log.info("Created token: {}", token);
        });

        log.info("Refresh tokens created");
    }

    @Given("^a clean database$")
    public void given_i_have_a_clean_database() throws NotFoundException {
        log.info("Cleaning database repositories...");

        this.refreshTokenRepository.deleteAll();
        this.gameSaveRepository.deleteAll();
        this.currencyRepository.deleteAll();
        this.userRepository.deleteAll();

        assertThat(refreshTokenRepository.count()).isEqualTo(0);
        assertThat(gameSaveRepository.count()).isEqualTo(0);
        assertThat(userRepository.count()).isEqualTo(0);
        assertThat(currencyRepository.count()).isEqualTo(0);

        // Clear caches
        redisCacheService.clearCaches();
        localCacheService.clearCaches();

        assertThat(currencyCache.getAll()).isEmpty();
        assertThat(currencyCache.getAllHisto()).isEmpty();
        assertThat(gameSaveOwnershipCache.getAll()).isEmpty();

        log.info("Database repositories + caches cleaned");

        // Init all repository mocks
        //MockUtils.initGameSaveRepositoryMock(gameSaveRepository, currencyRepository);
        //MockUtils.initUserRepositoryMock(userRepository);
        //MockUtils.initCurrencyRepositoryMock(currencyRepository);
        //MockUtils.initRefreshTokenRepository(refreshTokenRepository, userRepository);

        // Init all other service mocks
        MockUtils.initUserDetailsServiceMock(userDetailsService, userService, mapper);

        log.info("Mocks initialized");
    }

    @Given("^the following users$")
    public void given_i_have_the_following_users(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating users...");

        rows.stream().map(HashMap::new).forEach(modifiableRow -> {
            modifiableRow.computeIfPresent("password", (k, clearPassword) -> passwordEncoder.encode(clearPassword));
            UserEntity user = BddUtils.mapToUserEntity(modifiableRow);
            UserEntity newEntity = userRepository.save(user);
            log.info("User created: {}", newEntity);
        });

        log.info("Users created");
    }

    @Given("^the following game saves$")
    public void given_i_have_the_following_game_saves(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating game saves...");

        rows.forEach(row -> {
            GameSaveEntity gameSaveEntity = BddUtils.mapToGameSaveEntity(row, userRepository);
            GameSaveEntity newEntity = gameSaveRepository.save(gameSaveEntity);
            log.info("Game save created: {}", newEntity);
        });

        log.info("Game saves created");
    }

    @Given("^the following currency entries in cache$")
    public void given_i_have_the_following_currency_entries_in_cache(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating currency entries in cache...");

        rows.forEach(row -> {
            String gameSaveId = row.get(BddFieldConstants.CurrencyCacheEntry.GAME_SAVE_ID);
            Currency currency = BddUtils.mapToCurrency(row);
            currencyCache.set(gameSaveId, currency);
        });

        log.info("currency entries in cache created");
    }


    @Given("^the following game save ownerships in cache$")
    public void given_i_have_the_following_game_save_ownerships_in_cache(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating game save ownerships in cache...");

        rows.forEach(row -> {
            String gameSaveId = row.get(BddFieldConstants.GameSaveOwnershipCacheEntry.GAME_SAVE_ID);
            String userId = row.get(BddFieldConstants.GameSaveOwnershipCacheEntry.USER_EMAIL);
            gameSaveOwnershipCache.set(gameSaveId, userId);
            log.info("Game save ownership created: gameSaveId={}, userId={}", gameSaveId, userId);
        });

        log.info("Game save ownerships in cache created");
    }

    @Given("^the expiration seconds properties set to (.*)$")
    public void given_the_expiration_seconds_properties_set_to(int expirationSeconds) {
        log.info("Setting expiration seconds properties to {}", expirationSeconds);
        currencyCache.setExpiration(expirationSeconds);
        gameSaveOwnershipCache.setExpiration(expirationSeconds);
        invalidatedJwtTokenCache.setExpiration(expirationSeconds);
    }

}
