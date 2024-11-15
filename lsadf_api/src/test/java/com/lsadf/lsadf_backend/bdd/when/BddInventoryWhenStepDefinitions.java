package com.lsadf.lsadf_backend.bdd.when;

import com.lsadf.lsadf_backend.bdd.BddFieldConstants;
import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.InventoryEntity;
import com.lsadf.lsadf_backend.entities.ItemEntity;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.models.Item;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.lsadf.lsadf_backend.utils.ParameterizedTypeReferenceUtils.buildParameterizedInventoryResponse;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the when steps in the BDD scenarios
 */
@Slf4j(topic = "[INVENTORY WHEN STEP DEFINITIONS]")
public class BddInventoryWhenStepDefinitions extends BddLoader {
    @Given("^the following items to the inventory of the game save with id (.*)$")
    public void given_the_following_items(String gameSaveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        InventoryEntity inventoryEntity;

        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(gameSaveId);
        if (optionalInventoryEntity.isEmpty()) {
            log.info("Inventory not found, creating...");
            GameSaveEntity gameSaveEntity = gameSaveService.getGameSave(gameSaveId);
            inventoryEntity = InventoryEntity.builder().id(gameSaveId).gameSave(gameSaveEntity).build();
        } else {
            inventoryEntity = optionalInventoryEntity.get();
        }

        if (inventoryEntity.getItems() == null) {
            inventoryEntity.setItems(new HashSet<>());
        }

        log.info("Creating items...");
        rows.forEach(row -> {
            ItemEntity itemEntity = BddUtils.mapToItemEntity(row);
            inventoryEntity.getItems().add(itemEntity);
        });

        inventoryRepository.save(inventoryEntity);

        log.info("Items created");
    }

    @Given("^the inventory of the game save with id (.*) is set to empty$")
    public void when_the_inventory_of_the_game_save_with_id_is_set_to_empty(String gameSaveId) {
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
            GenericResponse<Inventory> body = result.getBody();
            inventoryStack.push(body.getData());
            responseStack.push(body);
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @Then("^the inventory of the game save with id (.*) should be empty$")
    public void when_the_inventory_of_the_game_save_with_id_should_be_empty(String gameSaveId) {
        try {
            GameSaveEntity gameSaveEntity = gameSaveService.getGameSave(gameSaveId);

            InventoryEntity inventoryEntity = gameSaveEntity.getInventoryEntity();

            assertThat(inventoryEntity).isNotNull();
            assertThat(inventoryEntity.getItems()).isEmpty();
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }


    @Then("^the response should have the following items in the inventory$")
    public void then_the_response_should_have_the_following_items_in_the_inventory(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Inventory inventory = inventoryStack.peek();
        for (Map<String, String> row : rows) {
            Item actual = inventory.getItems().stream()
                    .filter(g -> g.getId().equals(row.get("id")))
                    .findFirst()
                    .orElseThrow();

            Item expected = BddUtils.mapToItem(row);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .isEqualTo(expected);
        }
    }
}
