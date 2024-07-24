package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.game_save.GameSaveUpdateRequest;
import com.lsadf.lsadf_backend.services.AdminService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {

    public BddWhenStepDefinitions(UserRepository userRepository, GameSaveRepository gameSaveRepository, GameSaveService gameSaveService, Stack<List<GameSave>> gameSaveListStack, Stack<List<User>> userListStack, Stack<List<UserEntity>> userEntityListStack, Stack<List<GameSaveEntity>> gameSaveEntityListStack, Stack<Exception> exceptionStack, UserService userService, UserDetailsService userDetailsService, AdminService adminService, Mapper mapper) {
        super(userRepository, gameSaveRepository, gameSaveService, gameSaveListStack, userListStack, userEntityListStack, gameSaveEntityListStack, exceptionStack, userService, userDetailsService, adminService, mapper);
    }

    @When("^the user with email (.*) gets the game save with id (.*)$")
    public void when_the_user_with_email_gets_a_game_save_with_id(String userEmail, String gameSaveId) {
        try {
            GameSaveEntity gameSaveEntity = gameSaveService.getGameSave(gameSaveId);
            gameSaveEntityListStack.push(Collections.singletonList(gameSaveEntity));
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

    @When("^we want to update the game save with id (.*) with the following GameSaveUpdateRequest$")
    public void when_we_want_to_update_the_game_save_with_user_id_with_the_following_GameSaveUpdateRequest(String saveId, DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // it should have only one line
        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);
        GameSaveUpdateRequest updateRequest = BddUtils.mapToGameSaveUpdateRequest(row);
        try {
            GameSaveEntity updatedGameSave = gameSaveService.updateGameSave(saveId, updateRequest);
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

}
