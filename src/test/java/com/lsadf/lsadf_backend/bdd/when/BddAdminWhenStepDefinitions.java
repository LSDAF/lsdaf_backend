package com.lsadf.lsadf_backend.bdd.when;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Step definitions for the when steps in the BDD scenarios
 */
@Slf4j(topic = "[ADMIN WHEN STEP DEFINITIONS]")
public class BddAdminWhenStepDefinitions extends BddLoader {


    @When("^an admin gets the user with id (.*)")
    public void when_an_admin_gets_the_user_with_id(String userId) {
        try {
            UserAdminDetails user = adminService.getUserById(userId);
            userAdminDetailsStack.push(user);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin gets the user with email (.*)$")
    public void when_an_admin_gets_the_user_with_email(String userEmail) {
        try {
            UserAdminDetails user = adminService.getUserByEmail(userEmail);
            userAdminDetailsStack.push(user);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin searches for users ordered by (.*) with the following SearchRequest$")
    public void when_an_admin_searches_for_users_ordered_by_with_the_following_SearchRequest(String orderBy, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }
        ArrayList<Filter> filterList = new ArrayList<>();
        for (Map<String, String> row : rows) {
            Filter filter = BddUtils.mapToFilter(row);
            filterList.add(filter);
        }
        SearchRequest searchRequest = new SearchRequest(filterList);
        try {
            List<User> users = adminService.searchUsers(searchRequest, UserOrderBy.valueOf(orderBy));
            userListStack.push(users);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to create a new game save with the following AdminGameSaveCreationRequest$")
    public void when_we_want_to_create_a_new_game_save_with_the_following_AdminGameSaveCreationRequest(DataTable dataTable) throws NotFoundException {
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

    @When("^the user requests the admin endpoint to get all the save games ordered by (.*)$")
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

        assertThat(requests.size()).isEqualTo(1);

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

    @When("^an admin flushes and clears the cache$")
    public void when_an_admin_flushes_and_clears_the_cache() {
        try {
            adminService.flushAndClearCache();
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin toggles the cache status$")
    public void when_an_admin_toggles_the_cache_status() {
        try {
            adminService.toggleCache();
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

    @When("^an admin gets the cache status$")
    public void when_an_admin_gets_the_cache_status() {
        try {
            boolean cacheStatus = adminService.isRedisCacheEnabled();
            booleanStack.push(cacheStatus);
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

        assertThat(requests.size()).isEqualTo(1);

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

    @When("^an admin creates a new user with the following AdminUserCreationRequest$")
    public void when_an_admin_creates_a_new_user_with_the_following_AdminUserCreationRequest(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        assertThat(rows.size()).isEqualTo(1);

        Map<String, String> row = rows.get(0);

        User user = adminService.createUser(BddUtils.mapToAdminUserCreationRequest(row));

        try {
            userListStack.push(Collections.singletonList(user));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin deletes a user with the following id (.*)$")
    public void when_an_admin_deletes_a_user_with_the_following_id(String userId) {
        try {
            adminService.deleteUser(userId);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin deletes a user with the following email (.*)$")
    public void when_an_admin_deletes_a_user_with_the_following_email(String userEmail) {
        try {
            adminService.deleteUserByEmail(userEmail);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin gets the global info$")
    public void when_an_admin_gets_the_global_info() {
        try {
            GlobalInfo globalInfo = adminService.getGlobalInfo();
            globalInfoStack.push(globalInfo);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin gets all the game saves ordered by (.*)$")
    public void when_an_admin_gets_all_the_game_saves(String orderBy) {
        try {
            List<GameSave> gameSaves = adminService.getGameSaves(GameSaveOrderBy.valueOf(orderBy));
            gameSaveListStack.push(gameSaves);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin gets the game save with id (.*)$")
    public void when_an_admin_gets_the_game_save_with_id(String saveId) {
        try {
            GameSave gameSaveEntity = adminService.getGameSave(saveId);
            gameSaveListStack.push(Collections.singletonList(gameSaveEntity));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin updates the game save with id (.*) with the following GameSaveUpdateAdminRequest$")
    public void when_an_admin_updates_the_game_save_with_id_with_the_following_game_save_update_request(String saveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        AdminGameSaveUpdateRequest updateRequest = BddUtils.mapToAdminGameSaveUpdateRequest(row);
        try {
            GameSave updatedGameSave = adminService.updateGameSave(saveId, updateRequest);
            gameSaveListStack.push(Collections.singletonList(updatedGameSave));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin gets all the users ordered by (.*)$")
    public void when_an_admin_gets_all_the_users_ordered_by(String orderBy) {
        try {
            List<User> users = adminService.getUsers(UserOrderBy.valueOf(orderBy));
            userListStack.push(users);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin updates a user with id (.*) with the following AdminUserUpdateRequest")
    public void when_an_admin_updates_a_user_with_id_with_the_following_AdminUserUpdateRequest(String userId, DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        try {
            User user = adminService.updateUser(userId, BddUtils.mapToAdminUserUpdateRequest(row));
            userListStack.push(Collections.singletonList(user));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the admin endpoint to update the game save with id (.*) with the following AdminGameSaveUpdateRequest$")
    public void when_the_user_requests_the_admin_endpoint_to_update_the_game_save_with_id(String saveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        assertThat(rows.size()).isEqualTo(1);

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

    @When("^an admin creates a new game save with the following AdminGameSaveCreationRequest$")
    public void when_an_admin_creates_a_new_game_save_with_the_following_AdminGameSaveCreationRequest(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        List<GameSave> list = new ArrayList<>();

        try {
            for (Map<String, String> row : rows) {
                AdminGameSaveCreationRequest request = BddUtils.mapToAdminGameSaveCreationRequest(row);
                GameSave gameSave = adminService.createGameSave(request);
                list.add(gameSave);
            }
            gameSaveListStack.push(list);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin searches for game saves ordered by (.*) with the following SearchRequest$")
    public void when_an_admin_searches_for_game_saves_ordered_by_with_the_following_SearchRequest(String orderBy, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        ArrayList<Filter> filterList = new ArrayList<>();
        for (Map<String, String> row : rows) {
            Filter filter = BddUtils.mapToFilter(row);
            filterList.add(filter);
        }
        SearchRequest searchRequest = new SearchRequest(filterList);

        try {
            List<GameSave> gameSaves = adminService.searchGameSaves(searchRequest, GameSaveOrderBy.fromString(orderBy));
            gameSaveListStack.push(gameSaves);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin deletes the game save with id (.*)$")
    public void when_an_admin_deletes_the_game_save_with_id(String saveId) {
        try {
            adminService.deleteGameSave(saveId);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }
}
