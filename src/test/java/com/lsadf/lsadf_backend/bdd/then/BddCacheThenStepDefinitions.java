package com.lsadf.lsadf_backend.bdd.then;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.bdd.CacheEntryType;
import com.lsadf.lsadf_backend.models.Currency;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.lsadf.lsadf_backend.bdd.BddFieldConstants.CurrencyCacheEntry.*;
import static com.lsadf.lsadf_backend.bdd.BddFieldConstants.GameSaveOwnershipCacheEntry.USER_EMAIL;
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
            case CURRENCY -> assertThat(this.cacheService.getAllCurrencies()).isEmpty();
            case CURRENCY_HISTO -> assertThat(this.cacheService.getAllCurrenciesHisto()).isEmpty();
            case GAME_SAVE_OWNERSHIP -> assertThat(this.cacheService.getAllGameSaveOwnership()).isEmpty();
        }
    }

    @Then("^I should have the following (.*) entries in cache$")
    public void then_i_should_have_the_following_currency_entries_in_cache(String currencyTypeString, io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> stringStringMap = dataTable.asMaps();
        log.info("Checking {} entries in cache...", currencyTypeString);
        CacheEntryType cacheEntryType = CacheEntryType.fromString(currencyTypeString);
        switch (cacheEntryType) {
            case CURRENCY -> {
                var results = cacheService.getAllCurrencies();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    String goldString = entry.get(BddFieldConstants.Currency.GOLD);
                    String diamondString = entry.get(BddFieldConstants.Currency.DIAMOND);
                    String emeraldString = entry.get(BddFieldConstants.Currency.EMERALD);
                    String amethystString = entry.get(BddFieldConstants.Currency.AMETHYST);
                    long gold = goldString == null ? 0 : Long.parseLong(goldString);
                    long diamond = diamondString == null ? 0 : Long.parseLong(diamondString);
                    long emerald = emeraldString == null ? 0 : Long.parseLong(emeraldString);
                    long amethyst = amethystString == null ? 0 : Long.parseLong(amethystString);

                    Currency currency = new Currency(gold, diamond, emerald, amethyst);
                    assertThat(results).containsEntry(gameSaveId, currency);
                }
            }
            case CURRENCY_HISTO -> {
                var results = cacheService.getAllCurrenciesHisto();
                for (var entry : stringStringMap) {
                    String gameSaveId = entry.get(GAME_SAVE_ID);
                    String goldString = entry.get(BddFieldConstants.Currency.GOLD);
                    String diamondString = entry.get(BddFieldConstants.Currency.DIAMOND);
                    String emeraldString = entry.get(BddFieldConstants.Currency.EMERALD);
                    String amethystString = entry.get(BddFieldConstants.Currency.AMETHYST);
                    long gold = goldString == null ? 0 : Long.parseLong(goldString);
                    long diamond = diamondString == null ? 0 : Long.parseLong(diamondString);
                    long emerald = emeraldString == null ? 0 : Long.parseLong(emeraldString);
                    long amethyst = amethystString == null ? 0 : Long.parseLong(amethystString);
                    Currency currency = new Currency(gold, diamond, emerald, amethyst);
                    assertThat(results).containsEntry(gameSaveId, currency);
                }
            }
            case GAME_SAVE_OWNERSHIP -> {
                var results = cacheService.getAllGameSaveOwnership();
                for (var entry : stringStringMap) {
                    var gameSaveId = entry.get(GAME_SAVE_ID);
                    var email = entry.get(USER_EMAIL);
                    assertThat(results).containsEntry(gameSaveId, email);
                }
            }
        }
    }
}
