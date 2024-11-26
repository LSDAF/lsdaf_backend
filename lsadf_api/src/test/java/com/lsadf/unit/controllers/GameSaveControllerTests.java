package com.lsadf.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import com.lsadf.controllers.GameSaveController;
import com.lsadf.core.controllers.advices.GlobalExceptionHandler;
import com.lsadf.controllers.impl.GameSaveControllerImpl;
import com.lsadf.core.requests.game_save.GameSaveUpdateNicknameRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({GameSaveController.class, GameSaveControllerImpl.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class GameSaveControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void generateNewGameSave_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(post("/api/v1/game_save/generate"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void generateNewGameSave_should_return_200_when_user_is_authenticated() {
        // when
        mockMvc.perform(post("/api/v1/game_save/generate"))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void updateNickname_should_return_401_when_user_not_authenticated() {
        // given
        GameSaveUpdateNicknameRequest request = new GameSaveUpdateNicknameRequest("test");
        // when
        mockMvc.perform(post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateNickname_should_return_400_when_gameSaveId_is_not_uuid() {
        // given
        GameSaveUpdateNicknameRequest request = new GameSaveUpdateNicknameRequest("test");
        // when
        mockMvc.perform(post("/api/v1/game_save/{gameSaveId}/nickname", "testtesttest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateNickname_should_return_400_when_nickname_value_is_null() {
        // given
        GameSaveUpdateNicknameRequest request = new GameSaveUpdateNicknameRequest(null);
        // when
        mockMvc.perform(post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateNickname_should_return_400_when_GameSaveUpdateNicknameRequest_is_null() {
        // given
        GameSaveUpdateNicknameRequest request = null;
        // when
        mockMvc.perform(post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @SneakyThrows
    @ValueSource(strings = {
            "tr",
            "ertyuioqsdfghjklm",
            "test!",
            "test tu671"})
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateNickname_should_return_400_when_nickname_value_is_invalid(String nickname) {
        // given
        GameSaveUpdateNicknameRequest request = new GameSaveUpdateNicknameRequest(nickname);
        // when
        mockMvc.perform(post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateNickname_should_return_200_when_nickname_value_is_valid_and_user_authenticated() {
        // given
        String nickname = "toto1234";
        GameSaveUpdateNicknameRequest request = new GameSaveUpdateNicknameRequest(nickname);
        // when
        mockMvc.perform(post("/api/v1/game_save/{gameSaveId}/nickname", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getUserGameSaves_should_return_400_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/game_save/me"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getUserGameSaves_should_return_200_when_user_is_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/game_save/me"))
                // then
                .andExpect(status().isOk());
    }
}
