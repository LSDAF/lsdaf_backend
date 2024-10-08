package com.lsadf.lsadf_backend.bdd.then;

import com.lsadf.lsadf_backend.bdd.BddLoader;
import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.entities.tokens.UserVerificationTokenEntity;
import com.lsadf.lsadf_backend.exceptions.ForbiddenException;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.utils.BddUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.bdd.BddFieldConstants.RefreshToken.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.*;

/**
 * Step definitions for the then steps in the BDD scenarios
 */
@Slf4j(topic = "[THEN STEP DEFINITIONS]")
public class BddThenStepDefinitions extends BddLoader {


    @Then("^I should return the following game saves$")
    public void then_i_should_return_the_following_game_saves(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<GameSave> actual = gameSaveListStack.peek();

        for (Map<String, String> row : rows) {
            GameSave expectedGameSave = BddUtils.mapToGameSave(row);
            GameSave actualGameSave = actual.stream()
                    .filter(g -> g.getId().equals(expectedGameSave.getId()))
                    .findFirst().orElseThrow();

            assertThat(actualGameSave)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .ignoringExpectedNullFields()
                    .isEqualTo(expectedGameSave);
        }
    }

    @Then("^I should return the following game save entities$")
    public void then_i_should_return_the_following_game_save_entities(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<GameSaveEntity> actual = gameSaveEntityListStack.peek();

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


            long gold = gameSave.getCurrencyEntity().getGoldAmount();
            long expectedGold = expected.getCurrencyEntity().getGoldAmount();
            assertThat(gold).isEqualTo(expectedGold);

            long hp = gameSave.getHealthPoints();
            long expectedHp = expected.getHealthPoints();
            assertThat(hp).isEqualTo(expectedHp);

            long attack = gameSave.getAttack();
            long expectedAttack = expected.getAttack();
            assertThat(attack).isEqualTo(expectedAttack);
        }
    }

    @Then("^I should return the following game saves in exact order$")
    public void then_i_should_return_the_following_game_saves_in_exact_order(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<GameSave> actual = gameSaveListStack.peek();

        List<GameSave> expected = new ArrayList<>();

        for (Map<String, String> row : rows) {
            GameSave expectedEntity = BddUtils.mapToGameSave(row);
            expected.add(expectedEntity);
        }

        assertThat(actual).hasSameSizeAs(expected);

        for (int i = 0; i < actual.size(); i++) {
            GameSave actualGameSave = actual.get(i);
            GameSave expectedGameSave = expected.get(i);
            assertThat(actualGameSave)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .isEqualTo(expectedGameSave);
        }
    }

    @Then("^I should have no user entries in DB$")
    public void then_i_should_have_no_user_entries_in_db() {
        assertThat(userRepository.count()).isZero();
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

    @Then("^the response should have the following Boolean (.*)$")
    public void then_the_response_should_have_the_following_boolean(boolean expected) {
        boolean actual = (boolean) responseStack.peek().getData();
        assertThat(actual).isEqualTo(expected);
    }

    @Then("^the response status code should be (.*)$")
    public void then_the_response_status_code_should_be(int statusCode) {
        int actual = responseStack.peek().getStatus();
        assertThat(actual).isEqualTo(statusCode);
    }

    @Then("^I should have an unexpired and (.*) refresh token in DB for the user with email (.*)$")
    public void then_i_should_have_an_unexpired_and_status_refresh_token_in_db(String status, String userEmail) {
        UserEntity user = userRepository.findUserEntityByEmail(userEmail).orElseThrow();
        Optional<RefreshTokenEntity> actual = refreshTokenRepository.findByUserAndStatus(user, TokenStatus.valueOf(status));
        assertThat(actual).isNotEmpty();

    }

    @Then("^the used token should be invalidated$")
    public void then_the_used_token_should_be_invalidated() {
        String actual = jwtTokenStack.peek();

        Optional<String> result = invalidatedJwtTokenCache.get(actual);
        assertThat(result).isNotEmpty();
    }

    @Then("^I should have the following refresh tokens in DB$")
    public void then_i_should_have_the_following_refresh_tokens_in_db(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Iterable<RefreshTokenEntity> actualList = refreshTokenRepository.findAll();

        for (var elt : actualList) {
            Map<String, String> row = rows.stream()
                    .filter(r -> r.get(REFRESH_TOKEN).equals(elt.getToken()))
                    .findFirst()
                    .orElseThrow();

            RefreshTokenEntity expected = BddUtils.mapToRefreshTokenEntity(row, userRepository);

            assertThat(elt)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .isEqualTo(expected);
        }
    }

    @Then("^I should return the following refresh token$")
    public void then_i_should_return_the_following_refresh_token(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        RefreshTokenEntity actual = refreshTokenEntityStack.peek();
        RefreshTokenEntity expected = BddUtils.mapToRefreshTokenEntity(row, userRepository);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt","user","expirationDate")
                .isEqualTo(expected);
    }

    @Then("^the response should have the following UserInfo$")
    public void then_the_response_should_have_the_following_user_info(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        UserInfo actual = (UserInfo) responseStack.peek().getData();
        UserInfo expected = BddUtils.mapToUserInfo(row);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(expected);
    }

    @Then("^I should have the following game saves in DB$")
    public void then_i_should_have_the_following_entities(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        try (Stream<GameSaveEntity> actualStream = gameSaveRepository.findAllGameSaves()) {
            List<GameSaveEntity> actual = actualStream.toList();

            for (Map<String, String> row : rows) {
                GameSaveEntity expected = BddUtils.mapToGameSaveEntity(row, userRepository);
                GameSaveEntity gameSave = actual.stream()
                        .filter(g -> g.getId().equals(expected.getId()))
                        .findFirst()
                        .orElseThrow();

                assertThat(gameSave)
                        .usingRecursiveComparison()
                        .ignoringFields("user", "id", "createdAt", "updatedAt", "currencyEntity.gameSave")
                        .ignoringExpectedNullFields()
                        .isEqualTo(expected);
            }
        }
    }

    @Then("^I should have no game save entries in DB$")
    public void then_i_should_have_no_game_save_entries_in_db() {
        assertThat(gameSaveRepository.count()).isZero();
    }

    @Then("^I should have no currency entries in DB$")
    public void then_i_should_have_no_currency_entries_in_db() {
        assertThat(currencyRepository.count()).isZero();
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

    @Then("^the currency should be the following$")
    public void then_the_currency_amount_should_be(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        Currency expected = BddUtils.mapToCurrency(row);

        Currency actual = currencyStack.peek();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Then("^I should throw a ForbiddenException$")
    public void then_i_should_throw_a_forbidden_exception() {
        Exception exception = exceptionStack.peek();
        assertThat(exception).isInstanceOf(ForbiddenException.class);
    }

    @Then("^the response should have the following Currency$")
    public void then_the_response_should_have_the_following_currency(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        Currency expected = BddUtils.mapToCurrency(row);
        Currency actual = (Currency) responseStack.peek().getData();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Then("^I should return the following UserAdminDetails$")
    public void then_i_should_return_the_following_user_admin_details(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        UserAdminDetails userAdminDetails = userAdminDetailsStack.peek();
        UserAdminDetails expected = BddUtils.mapToUserAdminDetails(row);


        assertThat(userAdminDetails)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "password", "gameSaves")
                .isEqualTo(expected);
        assertThat(passwordEncoder.matches(expected.getPassword(), userAdminDetails.getPassword())).isTrue();
    }

    @Then("^I should return the following user entities$")
    public void then_i_should_should_return_users(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, UserEntity> expectedUserEntityMap = rows
                .stream()
                .map(BddUtils::mapToUserEntity)
                .collect(Collectors.toMap(UserEntity::getEmail, expected -> expected, (a, b) -> b));


        List<UserEntity> actual = userEntityListStack.peek();

        for (UserEntity userEntity : actual) {
            UserEntity expected = expectedUserEntityMap.get(userEntity.getEmail());
            assertThat(userEntity).isNotNull();
            assertThat(userEntity)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt", "password")
                    .isEqualTo(expected);
            assertThat(passwordEncoder.matches(expected.getPassword(), userEntity.getPassword())).isTrue();
        }
    }

    @Then("^I should return the following users$")
    public void then_i_should_return_the_following_users(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<User> actual = userListStack.peek();

        for (Map<String, String> row : rows) {
            User expectedUser = BddUtils.mapToUser(row, userRepository);
            User actualUser = actual.stream()
                    .filter(u -> u.getEmail().equals(expectedUser.getEmail()))
                    .findFirst().orElseThrow();

            assertThat(actualUser)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt", "password")
                    .isEqualTo(expectedUser);
        }

    }

    @Then("^I should return the following users in exact order$")
    public void then_i_should_return_the_following_users_in_exact_order(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<User> actual = userListStack.peek();

        List<User> expected = new ArrayList<>();

        for (Map<String, String> row : rows) {
            User expectedUser = BddUtils.mapToUser(row, userRepository);
            expected.add(expectedUser);
        }

        assertThat(actual).hasSameSizeAs(expected);

        for (int i = 0; i < actual.size(); i++) {
            User actualUser = actual.get(i);
            User expectedUser = expected.get(i);
            assertThat(actualUser)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt", "password")
                    .isEqualTo(expectedUser);
        }
    }

    @Then("^I should return the following GlobalInfo$")
    public void then_i_should_return_the_following_global_info(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        GlobalInfo actual = globalInfoStack.peek();
        GlobalInfo expected = BddUtils.mapToGlobalInfo(row);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Then("^I should throw no Exception$")
    public void then_i_should_throw_no_exception() {
        assertThat(exceptionStack).isEmpty();
    }

    @Then("^the token from the response should not be null$")
    public void then_the_token_from_the_response_should_not_be_null() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) responseStack.peek().getData();
        assertThat(jwtAuthentication.getAccessToken()).isNotNull();
    }

    @Then("^the refresh token from the response should not be null$")
    public void then_the_refresh_token_from_the_response_should_not_be_null() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) responseStack.peek().getData();
        assertThat(jwtAuthentication.getRefreshToken()).isNotNull();
    }

    @Then("^the JwtAuthentication should contain the following UserInfo$")
    public void then_the_jwt_authentication_should_contain_the_following_user_info(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        JwtAuthentication actual = (JwtAuthentication) responseStack.peek().getData();
        UserInfo actualUserInfo = actual.getUserInfo();
        UserInfo expectedUserInfo = BddUtils.mapToUserInfo(row);

        assertThat(actualUserInfo)
                .usingRecursiveComparison()
                .ignoringFields("id","createdAt", "updatedAt")
                .isEqualTo(expectedUserInfo);
    }

    @Then("^the response should have the following GameSave$")
    public void then_the_response_should_have_the_following_game_save(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        GameSave actual = (GameSave) responseStack.peek().getData();
        GameSave expected = BddUtils.mapToGameSave(row);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    @Then("the response should have the following GlobalInfo")
    public void then_the_response_should_have_the_tollowing_global_info(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        GlobalInfo actual = (GlobalInfo) responseStack.peek().getData();
        GlobalInfo expected = BddUtils.mapToGlobalInfo(row);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @And("the response should have the following GameSaves")
    public void then_the_response_should_have_the_following_game_saves(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            GameSave actual = gameSaveListStack.peek().stream()
                    .filter(g -> g.getId().equals(row.get("id")))
                    .findFirst()
                    .orElseThrow();
            GameSave expected = BddUtils.mapToGameSave(row);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .isEqualTo(expected);
        }

    }

    @And("the response should have the following UserAdminDetails")
    public void then_the_response_should_have_the_following_UserAdminDetails(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        if (rows.size() > 1) {
            throw new IllegalArgumentException("Expected only one row in the DataTable");
        }

        Map<String, String> row = rows.get(0);

        UserAdminDetails actual = (UserAdminDetails) responseStack.peek().getData();
        UserAdminDetails expected = BddUtils.mapToUserAdminDetails(row);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "password", "gameSaves")
                .isEqualTo(expected);
        assertThat(passwordEncoder.matches(expected.getPassword(), actual.getPassword())).isTrue();
    }

    @Then("^the response should have the following Users in exact order$")
    public void then_the_response_should_haven_the_following_users_in_exact_order(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<User> actual = userListStack.peek();

        if (rows.size() != actual.size()) {
            throw new IllegalArgumentException("Expected number of rows does not match the actual number of users");
        }

        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> row = rows.get(i);
            User actualUser = actual.get(i);
            User expectedUser = BddUtils.mapToUser(row, userRepository);

            assertThat(actualUser)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt", "password")
                    .isEqualTo(expectedUser);
        }
    }

    @Then("^a new validation token should have been created for the user with email (.*)$")
    public void then_a_new_validation_token_should_be_created_for_the_user_with_email(String email) {
        List<UserVerificationTokenEntity> results = new ArrayList<>();
        Iterable<UserVerificationTokenEntity> tokens = userVerificationTokenRepository.findAllByUserEmail(email);
        tokens.forEach(results::add);

        assertThat(results.size()).isNotZero();
    }

    @Then("^an email should have been sent to (.*)$")
    public void then_en_email_should_have_been_sent_to(String email) throws MessagingException {
        var mime = mimeMessageStack.peek();
        assertThat(mime).isNotNull();

        String actual = mime.getAllRecipients()[0].toString();
        assertThat(actual).isEqualTo(email);
    }

    @Then("^the response should have the following Users$")
    public void then_the_response_should_have_the_following_users(DataTable dataTable) throws NotFoundException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            User actual = userListStack.peek().stream()
                    .filter(u -> u.getEmail().equals(row.get("email")))
                    .findFirst()
                    .orElseThrow();
            User expected = BddUtils.mapToUser(row, userRepository);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt", "password")
                    .isEqualTo(expected);
        }
    }
}
