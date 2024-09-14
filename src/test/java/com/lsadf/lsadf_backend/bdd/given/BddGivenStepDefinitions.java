package com.lsadf.lsadf_backend.bdd.given;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.utils.BddUtils;
import com.lsadf.lsadf_backend.utils.MockUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the given steps in the BDD scenarios
 */
@Slf4j(topic = "[GIVEN STEP DEFINITIONS]")
public class BddGivenStepDefinitions extends BddLoader {

    @Given("^the BDD engine is ready$")
    public void given_the_bdd_engine_is_ready() {
        this.longGoldStack.clear();
        this.goldStack.clear();
        this.gameSaveListStack.clear();
        this.exceptionStack.clear();
        this.gameSaveEntityListStack.clear();
        this.userListStack.clear();
        this.userEntityListStack.clear();
        this.globalInfoStack.clear();
        this.userInfoListStack.clear();
        this.localUserMap.clear();
        this.userAdminDetailsStack.clear();
        this.responseStack.clear();
        this.jwtStack.clear();
        this.booleanStack.clear();
        this.localUserMap.clear();

        this.localUserCache.invalidateAll();

        BddUtils.initTestRestTemplate(testRestTemplate);

        log.info("BDD engine is ready. Using port: {}", this.serverPort);
    }

    @Given("^the cache is enabled$")
    public void given_the_cache_is_enabled() {
        log.info("Checking cache status...");
        boolean cacheEnabled = cacheService.isEnabled();
        if (!cacheEnabled) {
            log.info("Cache is disabled. Enabling cache...");
            cacheService.toggleCacheEnabling();
            assertThat(cacheService.isEnabled()).isTrue();
            log.info("Cache enabled");
        } else {
            log.info("Cache is already enabled");
        }
    }

    @Given("^a clean database$")
    public void given_i_have_a_clean_database() throws NotFoundException {
        log.info("Cleaning database repositories...");

        this.gameSaveRepository.deleteAll();
        this.goldRepository.deleteAll();
        this.userRepository.deleteAll();

        assertThat(gameSaveRepository.count()).isEqualTo(0);
        assertThat(userRepository.count()).isEqualTo(0);
        assertThat(goldRepository.count()).isEqualTo(0);

        // Clear caches
        cacheService.clearCaches();

        assertThat(cacheService.getAllGold()).isEmpty();
        assertThat(cacheService.getAllGameSaveOwnership()).isEmpty();

        log.info("Database repositories + caches cleaned");

        // Init all repository mocks
        MockUtils.initGameSaveRepositoryMock(gameSaveRepository, goldRepository);
        MockUtils.initUserRepositoryMock(userRepository);
        MockUtils.initGoldRepositoryMock(goldRepository);

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

    @Given("^the following gold entries in cache$")
    public void given_i_have_the_following_gold_entries_in_cache(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating gold entries in cache...");

        rows.forEach(row -> {
            String gameSaveId = row.get(BddFieldConstants.GoldCacheEntry.GAME_SAVE_ID);
            Long gold = Long.parseLong(row.get(BddFieldConstants.GoldCacheEntry.GOLD));
            cacheService.setGold(gameSaveId, gold);
            log.info("Gold entry created: gameSaveId={}, gold={}", gameSaveId, gold);
        });

        log.info("Gold entries in cache created");
    }

    @Given("^the following game save ownerships in cache$")
    public void given_i_have_the_following_game_save_ownerships_in_cache(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        log.info("Creating game save ownerships in cache...");

        rows.forEach(row -> {
            String gameSaveId = row.get(BddFieldConstants.GameSaveOwnershipCacheEntry.GAME_SAVE_ID);
            String userId = row.get(BddFieldConstants.GameSaveOwnershipCacheEntry.USER_ID);
            cacheService.setGameSaveOwnership(gameSaveId, userId);
            log.info("Game save ownership created: gameSaveId={}, userId={}", gameSaveId, userId);
        });

        log.info("Game save ownerships in cache created");
    }

    @Given("^the expiration seconds properties set to (.*)$")
    public void given_the_expiration_seconds_properties_set_to(int expirationSeconds) {
        log.info("Setting expiration seconds properties to {}", expirationSeconds);
        this.cacheExpirationProperties.setGoldExpirationSeconds(expirationSeconds);
        this.cacheExpirationProperties.setGameSaveOwnershipExpirationSeconds(expirationSeconds);
    }

}
