package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.GameSaveService;
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
    public BddThenStepDefinitions(UserRepository userRepository,
                                  GameSaveRepository gameSaveRepository,
                                  GameSaveService gameSaveService,
                                  Stack<GameSave> gameSaveStack,
                                  Stack<Exception> exceptionStack) {
        super(userRepository, gameSaveRepository, gameSaveService, gameSaveStack, exceptionStack);
    }

    @Then("^I should return the following game save$")
    public void i_should_return_the_following_game_save(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        GameSave expected = BddUtils.mapToGameSave(row, userRepository);

        GameSave gameSave = gameSaveStack.peek();

        assertThat(gameSave)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "user")
                .isEqualTo(expected);
    }

    @Then("^I should have the following game saves in DB$")
    public void i_should_have_the_following_entities(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        rows.forEach(row -> {
            GameSaveEntity expected = BddUtils.mapToGameSaveEntity(row, userRepository);
            GameSaveEntity gameSave = gameSaveRepository.findById(row.get(BddFieldConstants.GameSave.ID)).orElse(null);
            assertThat(gameSave).isNotNull();
            assertThat(gameSave)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .isEqualTo(expected);
        });
    }

    @Then("^I should have no game save entries in DB$")
    public void i_should_have_no_game_save_entries_in_db() {
        assertThat(gameSaveRepository.count()).isEqualTo(0);
    }

    @Then("^I should throw a NotFoundException$")
    public void i_should_throw_a_not_found_exception() {
        Exception exception = exceptionStack.peek();
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @Then("^I should throw a ForbiddenException$")
    public void i_should_throw_a_forbidden_exception() {
        Exception exception = exceptionStack.peek();
        assertThat(exception).isInstanceOf(ForbiddenException.class);
    }


}
