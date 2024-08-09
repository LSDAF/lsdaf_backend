package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.JwtAuthentication;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j(topic = "[THEN STEP DEFINITIONS]")
public class BddThenStepDefinitions extends BddLoader {

    @Then("^I should return the following game saves$")
    public void then_i_should_return_the_following_game_saves(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<GameSaveEntity> actual = gameSaveEntityListStack.pop();

        for (Map<String, String> row : rows) {
            GameSaveEntity expected = BddUtils.mapToGameSaveEntity(row, userRepository);
            GameSaveEntity gameSave = actual.stream()
                    .filter(g -> {
                        if (expected.getId() == null) {
                            return g.getUser().getId().equals(expected.getUser().getId());
                        }
                        return g.getId().equals(expected.getId());
                    })
                    .findFirst()
                    .orElseThrow();

            assertThat(gameSave)
                    .usingRecursiveComparison()
                    .ignoringFields("user", "id", "createdAt", "updatedAt")
                    .isEqualTo(expected);
        }
    }

    @Then("^I should have no user entries in DB$")
    public void then_i_should_have_no_user_entries_in_db() {
        assertThat(userRepository.count()).isEqualTo(0);
    }

    @Then("^I should return true$")
    public void then_i_should_return_true() {
        boolean actual = booleanStack.peek();
        assertThat(actual).isTrue();
    }

    @Then("^I should return false$")
    public void then_i_should_return_false() {
        boolean actual = booleanStack.peek();
        assertThat(actual).isFalse();
    }

    @Then("^the response status code should be (.*)$")
    public void then_the_response_status_code_should_be(int statusCode) {
        int actual = responseStack.peek().getStatus();
        assertThat(actual).isEqualTo(statusCode);
    }

    @Then("^the response should have the following UserInfo$")
    public void then_the_response_should_have_the_following_user_info(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        var row = rows.get(0);

        UserInfo actual = (UserInfo) responseStack.peek().getData();
        UserInfo expected = BddUtils.mapToUserInfo(row);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Then("^I should have the following game saves in DB$")
    public void then_i_should_have_the_following_entities(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<GameSaveEntity> actual = gameSaveEntityListStack.pop();

        for (Map<String, String> row : rows) {
            GameSaveEntity expected = BddUtils.mapToGameSaveEntity(row, userRepository);
            GameSaveEntity gameSave = actual.stream()
                    .filter(g -> g.getId().equals(expected.getId()))
                    .findFirst()
                    .orElseThrow();

            assertThat(gameSave)
                    .usingRecursiveComparison()
                    .ignoringFields("user", "id", "createdAt", "updatedAt")
                    .isEqualTo(expected);
        }
    }

    @Then("^I should have no game save entries in DB$")
    public void then_i_should_have_no_game_save_entries_in_db() {
        assertThat(gameSaveRepository.count()).isEqualTo(0);
    }

    @Then("^I should throw a NotFoundException$")
    public void then_i_should_throw_a_not_found_exception() {
        Exception exception = exceptionStack.peek();
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @Then("^I should throw a IllegalArgumentException$")
    public void then_i_should_throw_a_illegal_argument_exception() {
        Exception exception = exceptionStack.peek();
        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }

    @Then("^I should throw a ForbiddenException$")
    public void then_i_should_throw_a_forbidden_exception() {
        Exception exception = exceptionStack.peek();
        assertThat(exception).isInstanceOf(ForbiddenException.class);
    }

    @Then("^I should return the following users$")
    public void then_i_should_should_return_users(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    }

    @Then("^I should throw no Exception$")
    public void then_i_should_throw_no_exception() {
        assertThat(exceptionStack.isEmpty()).isTrue();
    }

    @Then("^the token from the response should be valid$")
    public void then_the_token_from_the_response_should_be_valid() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) responseStack.peek().getData();
        assertThat(jwtAuthentication.getAccessToken()).isNotNull();
        boolean validToken = tokenProvider.validateToken(jwtAuthentication.getAccessToken());
        assertThat(validToken).isTrue();
    }

    @Then("^the JwtAuthentication should contain the following UserInfo$")
    public void then_the_jwt_authentication_should_contain_the_following_user_info(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        var row = rows.get(0);

        JwtAuthentication actual = (JwtAuthentication) responseStack.peek().getData();
        UserInfo actualUserInfo = actual.getUserInfo();
        UserInfo expectedUserInfo = BddUtils.mapToUserInfo(row);

        assertThat(actualUserInfo)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedUserInfo);
    }

    @Then("^the response should have the following GameSave$")
    public void then_the_response_should_have_the_following_game_save(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        var row = rows.get(0);

        GameSave actual = (GameSave) responseStack.peek().getData();
        GameSave expected = BddUtils.mapToGameSave(row);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(expected);
    }
}
