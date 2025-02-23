package com.lsadf.admin.bdd.when;

import static com.lsadf.admin.bdd.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lsadf.admin.bdd.BddLoader;
import com.lsadf.admin.bdd.BddUtils;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.models.JwtAuthentication;
import com.lsadf.core.models.Stage;
import com.lsadf.core.requests.stage.StageRequest;
import com.lsadf.core.responses.GenericResponse;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/** Step definitions for the when steps in the BDD scenarios */
@Slf4j(topic = "[STAGE WHEN STEP DEFINITIONS]")
public class BddStageWhenStepDefinitions extends BddLoader {

  @When("^we want to get the stages for the game save with id (.*)$")
  public void when_we_want_to_get_the_stages_for_the_game_save_with_id(String gameSaveId) {
    try {
      log.info("Getting currencies for game save with id: {}", gameSaveId);
      Stage stage = this.stageService.getStage(gameSaveId);
      stageStack.push(stage);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^we want to set the following stages for the game save with id (.*) with toCache to (.*)$")
  public void when_we_want_to_set_the_stages_for_the_game_save_with_id_to_with_cache(
      String gameSaveId, boolean toCache, DataTable dataTable) {
    var data = dataTable.asMaps(String.class, String.class);
    assertThat(data).hasSize(1);

    Stage stage = BddUtils.mapToStage(data.get(0));

    try {
      log.info("Setting {} for game save with id: {}", stage, gameSaveId);
      this.stageService.saveStage(gameSaveId, stage, toCache);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When(
      "^the user requests the endpoint to set the stages with the following StageRequest for the game save with id (.*)$")
  public void
      when_the_user_requests_the_endpoint_to_set_the_stages_with_the_following_StageRequest_for_the_game_save_with_id(
          String gameSaveId, DataTable dataTable) {
    var data = dataTable.asMaps(String.class, String.class);
    assertThat(data).hasSize(1);

    StageRequest request = BddUtils.mapToStageRequest(data.get(0));

    String fullPath =
        ControllerConstants.STAGE
            + ControllerConstants.Stage.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);

      HttpEntity<StageRequest> httpRequest = new HttpEntity<>(request, headers);
      ResponseEntity<GenericResponse<Void>> result =
          testRestTemplate.exchange(
              url, HttpMethod.POST, httpRequest, buildParameterizedVoidResponse());
      var body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);

    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }

  @When("^the user requests the endpoint to get the stages of the game save with id (.*)$")
  public void when_the_user_requests_the_endpoint_to_get_the_stages_of_the_game_save_with_id(
      String gameSaveId) {
    String fullPath =
        ControllerConstants.STAGE
            + ControllerConstants.Stage.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
    String url = BddUtils.buildUrl(this.serverPort, fullPath);
    try {
      JwtAuthentication jwtAuthentication = jwtAuthenticationStack.peek();
      String token = jwtAuthentication.getAccessToken();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      HttpEntity<Void> request = new HttpEntity<>(headers);
      ResponseEntity<GenericResponse<Stage>> result =
          testRestTemplate.exchange(
              url, HttpMethod.GET, request, buildParameterizedStageResponse());
      var body = result.getBody();
      responseStack.push(body);
      log.info("Response: {}", result);
    } catch (Exception e) {
      exceptionStack.push(e);
    }
  }
}
