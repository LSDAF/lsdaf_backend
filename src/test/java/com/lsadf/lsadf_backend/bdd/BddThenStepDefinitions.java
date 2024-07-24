package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.AdminService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j(topic = "[THEN STEP DEFINITIONS]")
public class BddThenStepDefinitions extends BddLoader {

    public BddThenStepDefinitions(UserRepository userRepository, GameSaveRepository gameSaveRepository, GameSaveService gameSaveService, Stack<List<GameSave>> gameSaveListStack, Stack<List<User>> userListStack, Stack<List<UserEntity>> userEntityListStack, Stack<List<GameSaveEntity>> gameSaveEntityListStack, Stack<Exception> exceptionStack, UserService userService, UserDetailsService userDetailsService, AdminService adminService, Mapper mapper) {
        super(userRepository, gameSaveRepository, gameSaveService, gameSaveListStack, userListStack, userEntityListStack, gameSaveEntityListStack, exceptionStack, userService, userDetailsService, adminService, mapper);
    }

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
}
