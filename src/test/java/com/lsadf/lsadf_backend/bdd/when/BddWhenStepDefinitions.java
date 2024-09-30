package com.lsadf.lsadf_backend.bdd.when;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.constants.ControllerConstants;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateNicknameRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserRefreshLoginRequest;
import com.lsadf.lsadf_backend.requests.user.UserUpdateRequest;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import java.util.*;

import static com.lsadf.lsadf_backend.utils.ParameterizedTypeReferenceUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Step definitions for the when steps in the BDD scenarios
 */
@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {

    @When("^the user with email (.*) gets the game save with id (.*)$")
    public void when_the_user_with_email_gets_a_game_save_with_id(String userEmail, String gameSaveId) {
        try {
            GameSaveEntity gameSaveEntity = gameSaveService.getGameSave(gameSaveId);
            gameSaveEntityListStack.push(Collections.singletonList(gameSaveEntity));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to register a user with the following UserCreationRequest$")
    public void when_the_user_request_the_endpoint_to_register_a_user_with_the_following_UserCreationRequest(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        UserCreationRequest userCreationRequest = BddUtils.mapToUserCreationRequest(row);
        String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.REGISTER;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        HttpEntity<UserCreationRequest> request = BddUtils.buildHttpEntity(userCreationRequest);
        try {

            ResponseEntity<GenericResponse<UserInfo>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedUserInfoResponse());
            GenericResponse<UserInfo> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to create a new user with the following data$")
    public void when_we_want_to_create_a_new_user_with_the_following_data(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        UserCreationRequest userCreationRequest = BddUtils.mapToUserCreationRequest(row);
        try {
            UserEntity user = userService.createUser(userCreationRequest);
            userEntityListStack.push(Collections.singletonList(user));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to validate the user password with email (.*) and password (.*)$")
    public void when_we_want_to_validate_the_user_password_with_email_and_password(String userEmail, String password) {
        try {
            booleanStack.push(userService.validateUserPassword(userEmail, password));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user logs out$")
    public void when_the_user_logs_out() {
        String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.LOGOUT;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedVoidResponse());
            GenericResponse<Void> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to update the user with id (.*) with the following UserUpdateRequest$")
    public void when_we_want_to_update_the_user_with_id_with_the_following_user_update_request(String userId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        UserUpdateRequest userUpdateRequest = BddUtils.mapToUserUpdateRequest(row);
        try {
            UserEntity updated = userService.updateUser(userId, userUpdateRequest);
            userEntityListStack.push(Collections.singletonList(updated));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to update the password of the user with email (.*) from (.*) to (.*)$")
    public void when_we_want_to_update_the_password_of_the_user_with_email_from_to(String userEmail, String oldPassword, String newPassword) {
        try {
            UserEntity user = userService.updateUserPassword(userEmail, oldPassword, newPassword);
            userEntityListStack.push(Collections.singletonList(user));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to delete the game save with id (.*)$")
    public void when_the_user_with_email_deletes_a_game_save(String saveId) {
        try {
            gameSaveService.deleteGameSave(saveId);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to create a new game save for the user with email (.*)$")
    public void when_we_want_to_create_a_new_game_save_for_the_user_with_email(String userEmail) {
        try {
            GameSaveEntity gameSaveEntity = gameSaveService.createGameSave(userEmail);
            gameSaveEntityListStack.push(Collections.singletonList(gameSaveEntity));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to get the game save with id (.*)$")
    public void when_we_want_to_get_the_game_save_with_id(String saveId) {
        try {
            GameSaveEntity gameSaveEntity = gameSaveService.getGameSave(saveId);
            gameSaveEntityListStack.push(Collections.singletonList(gameSaveEntity));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to get all game saves$")
    public void when_we_want_to_get_all_game_saves() {
        try {
            List<GameSaveEntity> gameSaves = gameSaveService.getGameSaves().toList();
            gameSaveEntityListStack.push(gameSaves);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to get all game saves for the user with email (.*)$")
    public void when_we_want_to_get_all_game_saves_for_the_user_with_email(String email) {
        try {
            List<GameSaveEntity> gameSaves = gameSaveService.getGameSavesByUserEmail(email);
            gameSaveEntityListStack.push(gameSaves);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to update the game save with id (.*) with the following GameSaveUpdateNicknameRequest")
    public void when_we_want_to_update_the_game_save_with_user_id_with_the_following_GameSaveUpdateNicknameRequest(String saveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        GameSaveUpdateNicknameRequest updateRequest = BddUtils.mapToGameSaveUpdateUserRequest(row);
        try {
            GameSaveEntity updatedGameSave = gameSaveService.updateNickname(saveId, updateRequest);
            gameSaveEntityListStack.push(Collections.singletonList(updatedGameSave));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we check the game save ownership with id (.*) for the user with email (.*)$")
    public void when_we_check_the_game_save_ownership_with_id_for_the_user_with_email(String saveId, String userEmail) {
        try {
            gameSaveService.checkGameSaveOwnership(saveId, userEmail);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user logs in with the following refresh token credentials$")
    public void when_the_user_logs_in_with_the_following_refresh_token_credentials(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.REFRESH_LOGIN;
        String url = BddUtils.buildUrl(this.serverPort, fullPath);

        assertThat(rows.size()).isEqualTo(1);
        Map<String, String> row = rows.get(0);

        UserRefreshLoginRequest refreshLoginRequest = BddUtils.mapToUserRefreshLoginRequest(row);
        HttpEntity<UserRefreshLoginRequest> request = BddUtils.buildHttpEntity(refreshLoginRequest);
        try {
            ResponseEntity<GenericResponse<JwtAuthentication>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedJwtAuthenticationResponse());
            GenericResponse<JwtAuthentication> body = result.getBody();
            assertThat(body).isNotNull();
            JwtAuthentication jwtAuthentication = body.getData();
            if (jwtAuthentication != null) {
                jwtTokenStack.push(jwtAuthentication.getAccessToken());
                refreshJwtTokenStack.push(jwtAuthentication.getRefreshToken());
                LocalUser localUser = userDetailsService.loadUserByEmail(refreshLoginRequest.getEmail());
                localUserMap.put(jwtAuthentication.getAccessToken(), localUser);
            }
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("the user logs in with the following credentials")
    public void when_the_user_logs_in_with_the_following_credentials(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        UserLoginRequest userLoginRequest = BddUtils.mapToUserLoginRequest(rows.get(0));

        String fullPath = ControllerConstants.AUTH + ControllerConstants.Auth.LOGIN;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);

        HttpEntity<UserLoginRequest> request = BddUtils.buildHttpEntity(userLoginRequest);
        try {
            ResponseEntity<GenericResponse<JwtAuthentication>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedJwtAuthenticationResponse());
            GenericResponse<JwtAuthentication> body = result.getBody();
            assertThat(body).isNotNull();
            JwtAuthentication jwtAuthentication = body.getData();
            if (jwtAuthentication != null) {
                jwtTokenStack.push(jwtAuthentication.getAccessToken());
                refreshJwtTokenStack.push(jwtAuthentication.getRefreshToken());
                LocalUser localUser = userDetailsService.loadUserByEmail(userLoginRequest.getEmail());
                localUserMap.put(jwtAuthentication.getAccessToken(), localUser);
            }
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to generate a GameSave$")
    public void when_the_user_requests_the_endpoint_to_create_a_game_save() {
        String fullPath = ControllerConstants.GAME_SAVE + ControllerConstants.GameSave.GENERATE;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {

            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<GameSave>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
            GenericResponse<GameSave> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to generate a game save with no token$")
    public void when_the_user_requests_the_endpoint_to_create_a_game_save_with_no_token() {
        String fullPath = ControllerConstants.GAME_SAVE + ControllerConstants.GameSave.GENERATE;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<GenericResponse<GameSave>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedGameSaveResponse());
            GenericResponse<GameSave> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to update a GameSave with no token$")
    public void when_the_user_requests_the_endpoint_to_update_a_game_save_with_no_token() {
        String fullPath = ControllerConstants.GAME_SAVE + "/1";

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<GameSaveUpdateNicknameRequest> request = new HttpEntity<>(new GameSaveUpdateNicknameRequest(), headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedVoidResponse());
            GenericResponse<Void> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to update a GameSave with id (.*) with the following GameSaveUpdateNicknameRequest$")
    public void when_the_user_requests_the_endpoint_to_update_a_game_save_with_id_with_the_following_game_save_update_nickname_request(String gameSaveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        GameSaveUpdateNicknameRequest updateRequest = BddUtils.mapToGameSaveUpdateUserRequest(row);

        String fullPath = ControllerConstants.GAME_SAVE + "/" + gameSaveId + "/nickname";

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {

            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<GameSaveUpdateNicknameRequest> request = new HttpEntity<>(updateRequest, headers);
            ResponseEntity<GenericResponse<Void>> result = testRestTemplate.exchange(url, HttpMethod.POST, request, buildParameterizedVoidResponse());
            GenericResponse<Void> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to get his UserInfo with no token$")
    public void when_the_user_requests_the_endpoint_to_get_his_user_info_with_no_token() {
        String fullPath = ControllerConstants.USER + ControllerConstants.User.USER_ME;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<GenericResponse<UserInfo>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedUserInfoResponse());
            GenericResponse<UserInfo> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to get his UserInfo$")
    public void when_the_user_requests_the_endpoint_to_get_his_user_info() {
        String fullPath = ControllerConstants.USER + ControllerConstants.User.USER_ME;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {

            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<UserInfo>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedUserInfoResponse());
            GenericResponse<UserInfo> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to get his GameSaves$")
    public void when_the_user_requests_the_endpoint_to_get_his_game_saves() {
        String fullPath = ControllerConstants.USER + ControllerConstants.User.USER_ME_GAME_SAVES;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {

            String token = jwtTokenStack.peek();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<GenericResponse<List<GameSave>>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedGameSaveListResponse());
            GenericResponse<List<GameSave>> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user requests the endpoint to get his GameSaves with no token$")
    public void when_the_user_requests_the_endpoint_to_get_his_game_saves_with_no_token() {
        String fullPath = ControllerConstants.USER + ControllerConstants.User.USER_ME_GAME_SAVES;

        String url = BddUtils.buildUrl(this.serverPort, fullPath);
        try {
            HttpEntity<Void> request = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<GenericResponse<List<GameSave>>> result = testRestTemplate.exchange(url, HttpMethod.GET, request, buildParameterizedGameSaveListResponse());
            GenericResponse<List<GameSave>> body = result.getBody();
            responseStack.push(body);
            log.info("Response: {}", result);

        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to check the existence of the user with email (.*)$")
    public void when_we_want_to_check_the_existence_of_the_user_with_email(String userEmail) {
        try {
            boolean exists = userService.existsByEmail(userEmail);
            booleanStack.push(exists);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to get the user with id (.*)$")
    public void when_we_want_to_get_the_user_with_id(String userId) {
        try {
            UserEntity user = userService.getUserById(userId);
            userEntityListStack.push(Collections.singletonList(user));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to delete the user with id (.*)$")
    public void when_we_want_to_delete_the_user_with_id(String userId) {
        try {
            userService.deleteUser(userId);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to delete the user with email (.*)$")
    public void when_we_want_to_delete_the_user_with_email(String userEmail) {
        try {
            userService.deleteUserByEmail(userEmail);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to get the user with email (.*)$")
    public void when_we_want_to_get_the_user_with_email(String userEmail) {
        try {
            UserEntity user = userService.getUserByEmail(userEmail);
            userEntityListStack.push(Collections.singletonList(user));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^we want to get all the users$")
    public void when_we_want_to_get_all_the_users() {
        try {
            List<UserEntity> users = userService.getUsers().toList();
            userEntityListStack.push(users);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

}
