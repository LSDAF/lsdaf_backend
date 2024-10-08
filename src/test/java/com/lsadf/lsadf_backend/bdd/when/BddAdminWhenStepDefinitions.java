package com.lsadf.lsadf_backend.bdd.when;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingGameSaveException;
import com.lsadf.lsadf_backend.exceptions.AlreadyExistingUserException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.common.Filter;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy;
import com.lsadf.lsadf_backend.requests.search.SearchRequest;
import com.lsadf.lsadf_backend.requests.user.UserOrderBy;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.lsadf.lsadf_backend.utils.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Step definitions for the when steps in the BDD scenarios
 */
@Slf4j(topic = "[ADMIN WHEN STEP DEFINITIONS]")
public class BddAdminWhenStepDefinitions extends BddLoader {

    @When("^we want to create a new game save with the following AdminGameSaveCreationRequest$")
    public void when_we_want_to_create_a_new_game_save_with_the_following_AdminGameSaveCreationRequest(DataTable dataTable) throws NotFoundException, AlreadyExistingGameSaveException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        List<GameSaveEntity> list = new ArrayList<>();
        for (Map<String, String> row : rows) {
            AdminGameSaveCreationRequest request = BddUtils.mapToAdminGameSaveCreationRequest(row);
            GameSaveEntity gameSave = gameSaveService.createGameSave(request);
            list.add(gameSave);
        }


        try {
            gameSaveEntityListStack.push(list);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("the user requests the admin endpoint to get the global info")
    public void when_the_user_requests_the_admin_endpoint_for_global_info() {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.GLOBAL_INFO;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<GlobalInfo>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedGlobalInfoResponse());
            GenericResponse<GlobalInfo> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("the user requests the admin endpoint to get the global info with no token")
    public void when_the_user_requests_the_admin_endpoint_for_global_info_with_no_token() {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.GLOBAL_INFO;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<GenericResponse<GlobalInfo>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedGlobalInfoResponse());
            GenericResponse<GlobalInfo> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to get all the users ordered by (.*)$")
    public void when_the_user_requests_the_admin_endpoint_to_get_all_the_users_ordered_by(String orderBy) {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.USERS + "?order_by=" + orderBy;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<List<User>>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedUserListResponse());
            GenericResponse<List<User>> body = result.getBody();
            userListStack.push(body.getData());
            responseStack.push(body);

            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to get all the game saves ordered by (.*)$")
    public void when_the_user_requests_the_admin_endpoint_to_get_all_save_games_ordered_by(String orderBy) {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.GAME_SAVES + "?order_by=" + orderBy;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<List<GameSave>>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedGameSaveListResponse());
            GenericResponse<List<GameSave>> body = result.getBody();
            gameSaveListStack.push(body.getData());
            responseStack.push(body);

            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to get details of the user with id (.*)$")
    public void when_the_user_requests_the_admin_endpoint_to_get_users_details_of_the_user_with_id(String userId) {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.USER_ID.replace("{user_id}", userId);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<UserAdminDetails>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedUserAdminDetailsResponse());
            GenericResponse<UserAdminDetails> body = result.getBody();
            userAdminDetailsStack.push(body.getData());
            responseStack.push(body);

            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to get details of the user with email (.*)$")
    public void when_the_user_requests_the_admin_endpoint_to_get_users_details_of_the_user_with_email(String userEmail) {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.USER_EMAIL.replace("{user_email}", userEmail);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<UserAdminDetails>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedUserAdminDetailsResponse());
            GenericResponse<UserAdminDetails> body = result.getBody();
            userAdminDetailsStack.push(body.getData());
            responseStack.push(body);

            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @And("^the user requests the admin endpoint to delete the user with id (.*)$")
    public void when_the_user_requests_the_admin_endpoint_to_delete_the_user_with_id(String userId) {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.USER_ID.replace("{user_id}", userId);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.DELETE, request, buildParameterizedVoidResponse());
            responseStack.push(result.getBody());
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @And("^the user requests the admin endpoint to delete the game save with id (.*)$")
    public void when_the_user_requests_the_admin_endpoint_to_delete_the_game_save_with_id(String gameSaveId) {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.GAME_SAVE_ID.replace("{game_save_id}", gameSaveId);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.DELETE, request, buildParameterizedVoidResponse());
            responseStack.push(result.getBody());
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to search users ordered by (.*) with the following SearchRequest$")
    public void when_the_user_requests_the_admin_endpoint_to_search_users_ordered_by_with_the_following_search_request(String orderBy, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.SEARCH_USERS + "?order_by=" + orderBy;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);

        List<Filter> filters = rows.stream().map(BddUtils::mapToFilter).toList();

        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<SearchRequest> request = new HttpEntity<>(new SearchRequest(filters), headers);
            ResponseEntity<GenericResponse<List<User>>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedUserListResponse());
            GenericResponse<List<User>> body = result.getBody();
            userListStack.push(body.getData());
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to search game saves ordered by (.*) with the following SearchRequest$")
    public void when_the_user_requests_the_admin_endpoint_to_search_game_saves_ordered_by_with_the_following_search_request(String orderBy, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.SEARCH_GAME_SAVES + "?order_by=" + orderBy;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);

        List<Filter> filters = rows.stream().map(BddUtils::mapToFilter).toList();

        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<SearchRequest> request = new HttpEntity<>(new SearchRequest(filters), headers);
            ResponseEntity<GenericResponse<List<GameSave>>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedGameSaveListResponse());
            GenericResponse<List<GameSave>> body = result.getBody();
            gameSaveListStack.push(body.getData());
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to update the user with id (.*) with the following AdminUserUpdateRequest$")
    public void when_the_user_requests_the_admin_endpoint_to_update_the_user_with_id(String id, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);


        AdminUserUpdateRequest adminUserUpdateRequest = BddUtils.mapToAdminUserUpdateRequest(rows.get(0));

        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.USER_ID.replace("{user_id}", id);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);

        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<AdminUserUpdateRequest> request = new HttpEntity<>(adminUserUpdateRequest, headers);
            ResponseEntity<GenericResponse<User>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParamaterizedUserResponse());
            GenericResponse<User> body = result.getBody();
            userListStack.push(Collections.singletonList(body.getData()));
            responseStack.push(body);
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to create a new user with the following AdminUserCreationRequest$")
    public void when_the_user_requests_the_admin_endpoint_to_create_a_new_user_with_the_following_AdminUserCreationRequest(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.CREATE_USER;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);

        List<AdminUserCreationRequest> requests = rows.stream().map(BddUtils::mapToAdminUserCreationRequest).toList();

        assertThat(requests).hasSize(1);

        AdminUserCreationRequest adminRequest = requests.get(0);

        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<AdminUserCreationRequest> request = new HttpEntity<>(adminRequest, headers);
            ResponseEntity<GenericResponse<User>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParamaterizedUserResponse());
            GenericResponse<User> body = result.getBody();
            userListStack.push(Collections.singletonList(body.getData()));
            responseStack.push(body);
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to toggle the cache status$")
    public void when_the_user_requests_the_admin_endpoint_to_toggle_the_cache_status() {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.TOGGLE_CACHE;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.PUT, request, buildParameterizedVoidResponse());
            responseStack.push(result.getBody());
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to get the cache status$")
    public void when_the_user_requests_the_admin_endpoint_to_get_the_cache_status() {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.CACHE_ENABLED;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Boolean>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedBooleanResponse());
            responseStack.push(result.getBody());
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to flush and clear the cache$")
    public void when_the_user_requests_the_admin_endpoint_to_clear_the_cache() {
        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.CACHE;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.PUT, request, buildParameterizedVoidResponse());
            responseStack.push(result.getBody());
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to create a new game save with the following AdminGameSaveCreationRequest$")
    public void when_the_user_requests_the_admin_endpoint_to_create_a_new_game_save_with_the_following_game_save_creation_request(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.CREATE_GAME_SAVE;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);

        List<AdminGameSaveCreationRequest> requests = rows.stream().map(BddUtils::mapToAdminGameSaveCreationRequest).toList();

        assertThat(requests).hasSize(1);

        AdminGameSaveCreationRequest adminRequest = requests.get(0);

        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<AdminGameSaveCreationRequest> request = new HttpEntity<>(adminRequest, headers);
            ResponseEntity<GenericResponse<List<GameSave>>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedGameSaveListResponse());
            GenericResponse<List<GameSave>> body = result.getBody();
            gameSaveListStack.push(body.getData());
            responseStack.push(body);
            log.info("Response: {}", result);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }


    @When("^the user requests the admin endpoint to update the game save with id (.*) with the following AdminGameSaveUpdateRequest$")
    public void when_the_user_requests_the_admin_endpoint_to_update_the_game_save_with_id(String saveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        assertThat(rows).hasSize(1);

        Map<String, String> row = rows.get(0);

        AdminGameSaveUpdateRequest updateRequest = BddUtils.mapToAdminGameSaveUpdateRequest(row);

        String fullPath = ControllerConstants.ADMIN + ControllerConstants.Admin.GAME_SAVE_ID.replace("{game_save_id}", saveId);
        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<AdminGameSaveUpdateRequest> request = new HttpEntity<>(updateRequest, headers);
            ResponseEntity<GenericResponse<GameSave>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
            GenericResponse<GameSave> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }
}
