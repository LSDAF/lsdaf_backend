package com.lsadf.lsadf_backend.bdd.when;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.InventoryEntity;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static com.lsadf.lsadf_backend.utils.ParameterizedTypeReferenceUtils.buildParameterizedInventoryResponse;

/**
 * Step definitions for the when steps in the BDD scenarios
 */
@Slf4j(topic = "[INVENTORY WHEN STEP DEFINITIONS]")
public class BddInventoryWhenStepDefinitions extends BddLoader {
    @When("^the inventory of the game save with id (.*) should be empty$")
    public void when_the_inventory_of_the_game_save_with_id_should_be_empty(String gameSaveId) {
        try {
            InventoryEntity inventoryEntity = InventoryEntity.builder().build();

            GameSaveEntity gameSaveEntity = gameSaveService.getGameSave(gameSaveId);

            gameSaveEntity.setInventoryEntity(inventoryEntity);

            gameSaveRepository.save(gameSaveEntity);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to get the inventory of the game save with id (.*)$")
    public void when_the_user_requests_the_endpoint_to_get_the_inventory_of_the_game_save_with_id(String gameSaveId) {
        String fullPath = ControllerConstants.INVENTORY + ControllerConstants.Inventory.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
            String token = jwtAuthentication.getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Inventory>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedInventoryResponse());
            var body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }
}
