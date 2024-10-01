package com.lsadf.lsadf_backend.bdd.when;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

/**
 * When step definitions for the refresh token related in the BDD scenarios
 */
@Slf4j(topic = "[REFRESH TOKEN WHEN STEP DEFINITIONS]")
public class BddRefreshTokenProviderWhenStepDefinitions extends BddLoader {

    @When("^we want to invalidate the token (.*) of the user with email (.*)$")
    public void when_we_want_to_invalidate_the_token_of_the_user_with_email(String token, String email) {
        try {
            log.info("Invalidating token for user with email {}", email);
            refreshTokenProvider.invalidateToken(token, email);
        } catch (Exception e) {
            log.error("Error while invalidating token", e);
            exceptionStack.push(e);
        }
    }

    @When("^we save the following refresh token (.*) for the user with email (.*)$")
    public void when_we_save_the_following_refresh_token_for_the_user_with_email(String refreshToken, String email) {
        try {
            log.info("Saving refresh token {} for user with email {}", refreshToken, email);
            UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
            var token = refreshTokenProvider.createToken(mapper.mapUserEntityToLocalUser(userEntity));
            refreshTokenEntityStack.push(token);
        } catch (Exception e) {
            log.error("Error while saving refresh token", e);
            exceptionStack.push(e);
        }
    }

    @When("^we want to delete the expired refresh tokens$")
    public void when_we_want_to_delete_the_expired_refresh_tokens() {
        try {
            log.info("Deleting expired tokens");
            refreshTokenProvider.deleteExpiredTokens();
        } catch (Exception e) {
            log.error("Error while deleting expired tokens", e);
            exceptionStack.push(e);
        }
    }
}
