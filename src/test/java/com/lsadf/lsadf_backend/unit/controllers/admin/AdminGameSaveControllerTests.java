package com.lsadf.lsadf_backend.unit.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.lsadf_backend.controllers.admin.AdminGameSaveController;
import com.lsadf.lsadf_backend.controllers.admin.impl.AdminGameSaveControllerImpl;
import com.lsadf.lsadf_backend.controllers.exception_handler.GlobalExceptionHandler;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminGameSaveUpdateRequest;
import com.lsadf.lsadf_backend.requests.currency.CurrencyRequest;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.unit.config.UnitTestConfiguration;
import com.lsadf.lsadf_backend.unit.config.WithMockJwtUser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static com.lsadf.lsadf_backend.constants.ControllerConstants.Params.ORDER_BY;
import static com.lsadf.lsadf_backend.requests.game_save.GameSaveOrderBy.NICKNAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {GlobalExceptionHandler.class, AdminGameSaveController.class, AdminGameSaveControllerImpl.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AdminGameSaveControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void deleteGameSave_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(delete("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void deleteGameSave_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(delete("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void deleteGameSave_should_return_400_when_game_save_id_is_not_uuid() {
        // when
        mockMvc.perform(delete("/api/v1/admin/game_saves/{game_save_id}", "testtesttest")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void deleteGameSave_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(delete("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void generateNewGameSave_should_return_401_when_user_not_authenticated() {
        // given
        CurrencyRequest currencyRequest = new CurrencyRequest(100L, 100L, 100L, 100L);
        StageRequest stageRequest = new StageRequest(1L, 10L);
        AdminGameSaveCreationRequest request = AdminGameSaveCreationRequest.builder()
                .currency(currencyRequest)
                .stage(stageRequest)
                .healthPoints(100L)
                .nickname("test")
                .attack(100L)
                .id("3ab69f45-de06-4fce-bded-21d989fdad73")
                .build();

        // when
        mockMvc.perform(post("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void generateNewGameSave_should_return_403_when_user_not_admin() {
        // given
        CurrencyRequest currencyRequest = new CurrencyRequest(100L, 100L, 100L, 100L);
        StageRequest stageRequest = new StageRequest(1L, 10L);
        AdminGameSaveCreationRequest request = AdminGameSaveCreationRequest.builder()
                .currency(currencyRequest)
                .stage(stageRequest)
                .healthPoints(100L)
                .nickname("test")
                .attack(100L)
                .id("3ab69f45-de06-4fce-bded-21d989fdad73")
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isForbidden());
    }

    private static Stream<Arguments> provideGenerateGameSaveInvalidArguments() {
        CurrencyRequest currencyRequest = new CurrencyRequest(100L, 100L, 100L, 100L); // valid currencyRequest
        StageRequest stageRequest = new StageRequest(1L, 10L); // valid stageRequest

        CurrencyRequest invalidCurrencyRequest = new CurrencyRequest(100L, 100L, 100L, -100L); // invalid currencyRequest
        StageRequest invalidStageRequest = new StageRequest(10L, 1L); // invalid stageRequest

        return Stream.of(
                Arguments.of("testtesttest", null, 100L, 100L, "test", currencyRequest, stageRequest), //invalid id
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", null, 100L, 100L, "test", currencyRequest, stageRequest), //invalid email
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "test", 100L, 100L, "test", currencyRequest, stageRequest), //invalid email 2
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", null, 100L, "test", currencyRequest, stageRequest), //invalid hp (null)
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", -10L, 100L, "test", currencyRequest, stageRequest), //invalid hp 2
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", 100L, null, "test", currencyRequest, stageRequest), //invalid attack (null)
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", 100L, -100L, "test", currencyRequest, stageRequest), //invalid attack 2
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", 100L, 100L, "test", null, stageRequest), // invalid currencyRequest (null)
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", 100L, 100L, "test", invalidCurrencyRequest, stageRequest), // invalid currencyRequest
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", 100L, 100L, "test", currencyRequest, null), // invalid stageRequest (null)
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", 100L, 100L, "test", currencyRequest, invalidStageRequest) // invalid stageRequest
        );
    }

    @ParameterizedTest
    @MethodSource("provideGenerateGameSaveInvalidArguments")
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void generateNewGameSave_should_return_400_when_invalid_request(String id,
                                                                    String userEmail,
                                                                    Long hp,
                                                                    Long attack,
                                                                    String nickname,
                                                                    CurrencyRequest currency,
                                                                    StageRequest stage) {
        // given
        AdminGameSaveCreationRequest request = AdminGameSaveCreationRequest.builder()
                .id(id)
                .userEmail(userEmail)
                .healthPoints(hp)
                .attack(attack)
                .nickname(nickname)
                .currency(currency)
                .stage(stage)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void generateNewGameSave_should_return_400_when_request_body_is_null() {
        // given
        AdminGameSaveCreationRequest request = null;
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideGenerateGameSaveValidArguments() {
        CurrencyRequest currencyRequest = new CurrencyRequest(100L, 100L, 100L, 100L); // valid currencyRequest
        StageRequest stageRequest = new StageRequest(1L, 10L); // valid stageRequest

        return Stream.of(
                Arguments.of(null, "paul.ochon@test.com", 100L, 100L, "test", currencyRequest, stageRequest), // valid: null id
                Arguments.of("3ab69f45-de06-4fce-bded-21d989fdad73", "paul.ochon@test.com", 100L, 100L, "test", currencyRequest, stageRequest) // valid
        );
    }

    @ParameterizedTest
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    @SneakyThrows
    @MethodSource("provideGenerateGameSaveValidArguments")
    void generateNewGameSave_should_return_200_when_authenticated_user_is_admin_and_valid_inputs(String id,
                                                                                                 String userEmail,
                                                                                                 Long hp,
                                                                                                 Long attack,
                                                                                                 String nickname,
                                                                                                 CurrencyRequest currency,
                                                                                                 StageRequest stage) {
        // given
        AdminGameSaveCreationRequest request = AdminGameSaveCreationRequest.builder()
                .id(id)
                .userEmail(userEmail)
                .healthPoints(hp)
                .attack(attack)
                .nickname(nickname)
                .currency(currency)
                .stage(stage)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getGameSave_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getGameSave_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getGameSave_should_return_400_when_game_save_id_is_not_uuid() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves/{game_save_id}", "testtesttest")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getGameSave_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getGameSaves_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getGameSaves_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getGameSaves_should_return_400_when_orderBy_is_invalid() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves")
                        .param(ORDER_BY, "INVALID_ORDER_BY")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getGameSaves_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getGameSaves_should_return_200_when_authenticated_user_is_admin_and_valid_orderBy() {
        // when
        mockMvc.perform(get("/api/v1/admin/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param(ORDER_BY, NICKNAME.name())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void updateGameSave_should_return_401_when_user_not_authenticated() {
        // given
        AdminGameSaveUpdateRequest request = AdminGameSaveUpdateRequest.builder()
                .attack(100L)
                .nickname("test")
                .healthPoints(100L)
                .build();

        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateGameSave_should_return_403_when_user_not_admin() {
        // given
        AdminGameSaveUpdateRequest request = AdminGameSaveUpdateRequest.builder()
                .attack(100L)
                .nickname("test")
                .healthPoints(100L)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateGameSave_should_return_400_when_gameSaveId_is_not_uuid() {
        // given
        AdminGameSaveUpdateRequest request = AdminGameSaveUpdateRequest.builder()
                .attack(100L)
                .nickname("test")
                .healthPoints(100L)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}", "testtesttest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideUpdateGameSaveInvalidArguments() {
        return Stream.of(
                Arguments.of("zuoezzh!@#&", 100L, 100L), // invalid nickname
                Arguments.of(null, 100L, 100L), // invalid nickname 2 (null)
                Arguments.of("test", null, 100L), // invalid hp (null)
                Arguments.of("test", -100L, 100L), // invalid hp (negative)
                Arguments.of("test", 0L, 100L), // invalid hp (0)
                Arguments.of("test", -100L, null), // invalid atk (null)
                Arguments.of("test", -100L, -100L), // invalid atk (negative)
                Arguments.of("test", -100L, -100L) // invalid atk (0)
        );
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("provideUpdateGameSaveInvalidArguments")
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateGameSave_should_return_400_when_invalid_request(String nickname, Long hp, Long atk) {
        // given
        AdminGameSaveUpdateRequest request = AdminGameSaveUpdateRequest.builder()
                .attack(atk)
                .nickname(nickname)
                .healthPoints(hp)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateGameSave_should_return_400_when_GameSaveUpdateRequest_is_null() {
        // given
        AdminGameSaveUpdateRequest request = null;
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateGameSave_should_return_200_when_authenticated_user_is_admin() {
        // given
        AdminGameSaveUpdateRequest request = AdminGameSaveUpdateRequest.builder()
                .attack(100L)
                .nickname("test")
                .healthPoints(100L)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void updateCurrencies_should_return_401_when_user_not_authenticated() {
        // given
        CurrencyRequest request = new CurrencyRequest(100L, 100L, 100L, 100L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/currencies", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateCurrencies_should_return_403_when_user_not_admin() {
        // given
        CurrencyRequest request = new CurrencyRequest(100L, 100L, 100L, 100L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/currencies", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateCurrencies_should_return_400_when_gameSaveId_is_not_uuid() {
        // given
        CurrencyRequest request = new CurrencyRequest(100L, 100L, 100L, 100L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/currencies", "testtesttest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateCurrencies_should_return_400_when_currencyRequest_is_null() {
        // given
        CurrencyRequest request = null;
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/currencies", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateCurrencies_should_return_400_when_request_is_invalid() {
        // given
        CurrencyRequest request = new CurrencyRequest(100L, 100L, 100L, -100L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/currencies", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateCurrencies_should_return_200_when_authenticated_user_is_admin() {
        // given
        CurrencyRequest request = new CurrencyRequest(100L, 100L, 100L, 100L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/currencies", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void updateGameSaveStages_should_return_401_when_user_not_authenticated() {
        // given
        StageRequest request = new StageRequest(1L, 10L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/stages", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateGameSaveStages_should_return_403_when_user_not_admin() {
        // given
        StageRequest request = new StageRequest(1L, 10L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/stages", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateGameSaveStages_should_return_400_when_gameSaveId_is_not_uuid() {
        // given
        StageRequest request = new StageRequest(1L, 10L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/stages", "testtesttest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateGameSaveStages_should_return_400_when_stageRequest_is_null() {
        // given
        StageRequest request = null;
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/stages", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateGameSaveStages_should_return_400_when_request_is_invalid() {
        // given
        StageRequest request = new StageRequest(10L, 1L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/stages", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateGameSaveStages_should_return_200_when_authenticated_user_is_admin() {
        // given
        StageRequest request = new StageRequest(1L, 10L);
        // when
        mockMvc.perform(post("/api/v1/admin/game_saves/{game_save_id}/stages", "3ab69f45-de06-4fce-bded-21d989fdad73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk());
    }
}