package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.AdminService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.*;

@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {

    public BddWhenStepDefinitions(UserRepository userRepository, GameSaveRepository gameSaveRepository, GameSaveService gameSaveService, Stack<List<GameSave>> gameSaveListStack, Stack<List<User>> userListStack, Stack<Exception> exceptionStack, UserService userService, UserDetailsService userDetailsService, AdminService adminService) {
        super(userRepository, gameSaveRepository, gameSaveService, gameSaveListStack, userListStack, exceptionStack, userService, userDetailsService, adminService);
    }

    @When("^the user with email (.*) gets the game save with id (.*)$")
    public void the_user_with_email_gets_a_game_save_with_id(String userEmail, String gameSaveId) {
        try {
            GameSave gameSave = gameSaveService.getGameSave(gameSaveId, userEmail);
            gameSaveStack.push(Collections.singletonList(gameSave));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user with email (.*) creates a new game save$")
    public void the_user_with_email_creates_a_new_game_save(String userEmail) {
        try {
            GameSave gameSave = gameSaveService.createGameSave(userEmail);
            gameSaveStack.push(Collections.singletonList(gameSave));
        } catch (Exception e) {
            log.warn("Exception {} thrown while creating new game save: ", e.getClass(), e);
            exceptionStack.push(e);
        }
    }

    @When("^the user with email (.*) updates the game save with id (.*)$")
    public void the_user_with_email_updates_a_game_save(String userEmail, String saveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        GameSave gameSave = BddUtils.mapToGameSave(row, userRepository);
        try {
            GameSave updatedGameSave = gameSaveService.updateGameSave(saveId, gameSave, userEmail);
            gameSaveStack.push(Collections.singletonList(updatedGameSave));
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user with email (.*) deletes a game save with id (.*)$")
    public void the_user_with_email_deletes_a_game_save(String userEmail, String saveId) {
        try {
            gameSaveService.deleteGameSave(saveId, userEmail);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin gets all the users with no sorting$")
    public void admin_gets_all_users() {
        try {
            List<User> users = adminService.getUsers(null);
            userListStack.push(users);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^an admin gets all the game saves with no sorting$")
    public void admin_gets_all_game_saves() {
        try {
            List<GameSave> users = adminService.getGameSaves(null);
            gameSaveStack.push(users);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

}
