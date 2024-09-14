package com.lsadf.lsadf_backend.bdd.then;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import static com.lsadf.lsadf_backend.bdd.BddFieldConstants.GoldCacheEntry.GAME_SAVE_ID;
import static com.lsadf.lsadf_backend.bdd.BddFieldConstants.GoldCacheEntry.GOLD;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the cache related then steps in the BDD scenarios
 */
@Slf4j(topic = "[CACHE THEN STEP DEFINITIONS]")
public class BddCacheThenStepDefinitions extends BddLoader {

    @Then("^the gold cache should be empty$")
    public void then_the_gold_cache_should_be_empty() {
        log.info("Checking if cache is empty...");
        var results = this.cacheService.getAllGold();

        assertThat(results).isEmpty();
    }

    @Then("^the gold histo cache should be empty$")
    public void then_the_gold_histo_cache_should_be_empty() {
        log.info("Checking if cache is empty...");
        var results = this.cacheService.getAllGoldHisto();

        assertThat(results).isEmpty();
    }

    @Then("^the game save ownership cache should be empty$")
    public void then_the_game_save_ownership_cache_should_be_empty() {
        log.info("Checking if cache is empty...");
        var results = this.cacheService.getAllGameSaveOwnership();

        assertThat(results).isEmpty();
    }

    @Then("^I should have the following gold entries in cache$")
    public void then_i_should_have_the_following_gold_entries_in_cache(io.cucumber.datatable.DataTable dataTable) {
        log.info("Checking gold entries in cache...");
        var results = this.cacheService.getAllGold();

        var expected = dataTable.asMaps();
        for (var entry : expected) {
            var gameSaveId = entry.get(GAME_SAVE_ID);
            var gold = Long.parseLong(entry.get(GOLD));

            assertThat(results).containsEntry(gameSaveId, gold);
        }
    }
}
