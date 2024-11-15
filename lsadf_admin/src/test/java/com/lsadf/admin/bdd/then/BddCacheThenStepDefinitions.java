package com.lsadf.admin.bdd.then;

import com.lsadf.admin.bdd.BddLoader;
import com.lsadf.admin.bdd.BddUtils;
import com.lsadf.admin.bdd.CacheEntryType;
import com.lsadf.core.models.Characteristics;
import com.lsadf.core.models.Currency;
import com.lsadf.core.models.Stage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.lsadf.admin.bdd.BddFieldConstants.GameSave.USER_EMAIL;
import static com.lsadf.admin.bdd.BddFieldConstants.StageCacheEntry.GAME_SAVE_ID;
import static com.lsadf.admin.bdd.CacheEntryType.CHARACTERISTICS;
import static com.lsadf.admin.bdd.CacheEntryType.STAGE_HISTO;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the cache related then steps in the BDD scenarios
 */
@Slf4j(topic = "[CACHE THEN STEP DEFINITIONS]")
public class BddCacheThenStepDefinitions extends BddLoader {

    @Then("^the (.*) cache should be empty$")
    public void then_the_cache_should_be_empty(String cacheType) {
        log.info("Checking {} if cache is empty...", cacheType);
        CacheEntryType cacheEntryType = CacheEntryType.fromString(cacheType);
        switch (cacheEntryType) {
            case CHARACTERISTICS -> assertThat(characteristicsCache.getAll()).isEmpty();
            case CHARACTERISTICS_HISTO -> assertThat(characteristicsCache.getAllHisto()).isEmpty();
            case CURRENCY -> assertThat(currencyCache.getAll()).isEmpty();
            case CURRENCY_HISTO -> assertThat(currencyCache.getAllHisto()).isEmpty();
            case GAME_SAVE_OWNERSHIP -> assertThat(gameSaveOwnershipCache.getAll()).isEmpty();
            case STAGE -> assertThat(stageCache.getAll()).isEmpty();
            case STAGE_HISTO -> assertThat(stageCache.getAllHisto()).isEmpty();
        }
    }

    @Then("^the redis cache should be disabled$")
    public void then_the_redis_cache_should_be_disabled() {
        log.info("Checking if redis cache is disabled...");
        assertThat(redisCacheService.isEnabled()).isFalse();
        assertThat(stageCache.isEnabled()).isFalse();
        assertThat(characteristicsCache.isEnabled()).isFalse();
        assertThat(currencyCache.isEnabled()).isFalse();
        assertThat(gameSaveOwnershipCache.isEnabled()).isFalse();
    }

    @Then("^I should have the following (.*) entries in cache$")
    public void then_i_should_have_the_following_cache_entries_in_cache(String currencyTypeString, DataTable dataTable) {
        List<Map<String, String>> stringStringMap = dataTable.asMaps();
        log.info("Checking {} entries in cache...", currencyTypeString);
        CacheEntryType cacheEntryType = CacheEntryType.fromString(currencyTypeString);
        switch (cacheEntryType) {
            case CHARACTERISTICS -> {
                var results = characteristicsCache.getAll();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Characteristics characteristics = BddUtils.mapToCharacteristics(entry);
                    assertThat(results).containsEntry(gameSaveId, characteristics);
                }
            }
            case CHARACTERISTICS_HISTO -> {
                var results = characteristicsCache.getAllHisto();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Characteristics characteristics = BddUtils.mapToCharacteristics(entry);
                    assertThat(results).containsEntry(gameSaveId, characteristics);
                }
            }
            case CURRENCY -> {
                var results = currencyCache.getAll();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Currency currency = BddUtils.mapToCurrency(entry);
                    assertThat(results).containsEntry(gameSaveId, currency);
                }
            }
            case CURRENCY_HISTO -> {
                var results = currencyCache.getAllHisto();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Currency currency = BddUtils.mapToCurrency(entry);
                    assertThat(results).containsEntry(gameSaveId, currency);
                }
            }
            case GAME_SAVE_OWNERSHIP -> {
                var results = gameSaveOwnershipCache.getAll();
                for (var entry : stringStringMap) {
                    var gameSaveId = entry.get(GAME_SAVE_ID);
                    var email = entry.get(USER_EMAIL);
                    assertThat(results).containsEntry(gameSaveId, email);
                }
            }
            case STAGE -> {
                var results = stageCache.getAll();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Stage stage = BddUtils.mapToStage(entry);
                    assertThat(results).containsEntry(gameSaveId, stage);
                }
            }
            case STAGE_HISTO -> {
                var results = stageCache.getAllHisto();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Stage stage = BddUtils.mapToStage(entry);
                    assertThat(results).containsEntry(gameSaveId, stage);
                }
            }
        }
    }

    @Then("^the (.*) cache should contain the following values$")
    public void then_the_entries_in_cache_should_be_updated_with_the_following_values(String cacheType, DataTable dataTable) {
        List<Map<String, String>> stringStringMap = dataTable.asMaps();
        log.info("Checking {} entries in cache if they are updated...", cacheType);
        CacheEntryType cacheEntryType = CacheEntryType.fromString(cacheType);
        switch (cacheEntryType) {
            case CHARACTERISTICS -> {
                var results = characteristicsCache.getAll();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Characteristics characteristics = BddUtils.mapToCharacteristics(entry);
                    assertThat(results).containsEntry(gameSaveId, characteristics);
                }
            }
            case CHARACTERISTICS_HISTO -> {
                var results = characteristicsCache.getAllHisto();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Characteristics characteristics = BddUtils.mapToCharacteristics(entry);
                    assertThat(results).containsEntry(gameSaveId, characteristics);
                }
            }
            case CURRENCY -> {
                var results = currencyCache.getAll();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Currency currency = BddUtils.mapToCurrency(entry);
                    assertThat(results).containsEntry(gameSaveId, currency);
                }
            }
            case CURRENCY_HISTO -> {
                var results = currencyCache.getAllHisto();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Currency currency = BddUtils.mapToCurrency(entry);
                    assertThat(results).containsEntry(gameSaveId, currency);
                }
            }
            case GAME_SAVE_OWNERSHIP -> {
                var results = gameSaveOwnershipCache.getAll();
                for (var entry : stringStringMap) {
                    var gameSaveId = entry.get(GAME_SAVE_ID);
                    var email = entry.get(USER_EMAIL);
                    assertThat(results).containsEntry(gameSaveId, email);
                }
            }
            case STAGE -> {
                var results = stageCache.getAll();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Stage stage = BddUtils.mapToStage(entry);
                    assertThat(results).containsEntry(gameSaveId, stage);
                }
            }
            case STAGE_HISTO -> {
                var results = stageCache.getAllHisto();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    Stage stage = BddUtils.mapToStage(entry);
                    assertThat(results).containsEntry(gameSaveId, stage);
                }
            }
        }
    }
}
