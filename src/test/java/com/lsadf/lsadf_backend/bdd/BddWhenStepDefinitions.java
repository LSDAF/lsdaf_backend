package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.GameSaveService;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

@Slf4j(topic = "[WHEN STEP DEFINITIONS]")
public class BddWhenStepDefinitions extends BddLoader {

    public BddWhenStepDefinitions(UserRepository userRepository,
                                  GameSaveRepository gameSaveRepository,
                                  GameSaveService gameSaveService,
                                  Stack<GameSave> gameSaveStack,
                                  Stack<Exception> exceptionStack) {
        super(userRepository, gameSaveRepository, gameSaveService, gameSaveStack, exceptionStack);
    }

    @When("^the user with email (.*) gets the game save with id (.*)$")
    public void the_user_with_email_gets_a_game_save_with_id(String userEmail, String gameSaveId) {
        try {
            GameSave gameSave = gameSaveService.getGameSave(gameSaveId, userEmail);
            gameSaveStack.push(gameSave);
        } catch (Exception e) {
            exceptionStack.push(e);
        }
    }

    @When("^the user with email (.*) creates a new game save$")
    public void the_user_with_email_creates_a_new_game_save(String userEmail) {
        try {
            GameSave gameSave = gameSaveService.createGameSave(userEmail);
            gameSaveStack.push(gameSave);
        } catch (Exception e) {
            log.warn("Exception {} thrown while creating new game save: ", e.getClass(), e);
            exceptionStack.push(e);
        }
    }

}
