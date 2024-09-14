package com.lsadf.lsadf_backend.bdd.when;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.Gold;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.concurrent.TimeUnit;

import static com.lsadf.lsadf_backend.utils.ParameterizedTypeReferenceUtils.*;
import static org.awaitility.Awaitility.await;

@Slf4j(topic = "[GOLD WHEN STEP DEFINITIONS]")
public class BddGoldWhenStepDefinitions extends BddLoader {
    @When("^the gold cache is flushed$")
    public void when_the_cache_is_flushed() {
        log.info("Flushing cache...");
        this.cacheFlushService.flushGold();
    }

    @When("^a gold cache entry is expired$")
    public void when_a_gold_cache_entry_is_expired() {
        int size = this.cacheService.getAllGoldHisto().size();
        log.info("Waiting for gold cache entry to expire...");
        await().atMost(1200, TimeUnit.SECONDS).until(() -> {
            try {
                var newSize = this.cacheService.getAllGoldHisto().size();
                return newSize < size;
            } catch (Exception e) {
                return false;
            }
        });
        log.info("Gold cache entry expired");
    }

    @When("^we want to get the gold for the game save with id (.*)$")
    public void when_we_want_to_get_the_gold_for_the_game_save_with_id(String gameSaveId) {
        try {
            log.info("Getting gold for game save with id: {}", gameSaveId);
            var gold = this.goldService.getGold(gameSaveId);
            longGoldStack.push(gold);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to set the gold for the game save with id (.*) to (.*) with cache$")
    public void when_we_want_to_set_the_gold_for_the_game_save_with_id_to_with_cache(String gameSaveId, long amount) {
        try {
            log.info("Setting gold for game save with id: {} to: {}", gameSaveId, amount);
            this.goldService.saveGold(gameSaveId, amount, true);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to set the gold for the game save with id (.*) to (.*) without cache$")
    public void when_we_want_to_set_the_gold_for_the_game_save_with_id_to_without_cache(String gameSaveId, long amount) {
        try {
            log.info("Setting gold for game save with id: {} to: {}", gameSaveId, amount);
            this.goldService.saveGold(gameSaveId, amount, false);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to get the gold of the game save with id (.*)$")
    public void when_the_user_requests_the_endpoint_to_get_the_gold_of_the_game_save_with_id(String gameSaveId) {
        String fullPath = ControllerConstants.GOLD + ControllerConstants.Gold.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Gold>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedGoldResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to set the gold of the game save with id (.*) to (.*)$")
    public void when_the_user_requests_the_endpoint_to_set_the_gold_of_the_game_save_with_id_to(String gameSaveId, long amount) {
        String fullPath = ControllerConstants.GOLD + ControllerConstants.Gold.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId) + "?gold_amount=" + amount;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.PUT, request, buildParameterizedVoidResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }
}
