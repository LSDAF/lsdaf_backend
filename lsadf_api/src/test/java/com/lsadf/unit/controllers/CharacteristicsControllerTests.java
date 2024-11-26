package com.lsadf.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import com.lsadf.controllers.CharacteristicsController;
import com.lsadf.core.controllers.advices.GlobalExceptionHandler;
import com.lsadf.controllers.impl.CharacteristicsControllerImpl;
import com.lsadf.core.requests.characteristics.CharacteristicsRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({CharacteristicsControllerImpl.class, CharacteristicsController.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class CharacteristicsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getCharacteristics_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void saveCharacteristics_should_return_401_when_user_not_authenticated() {
        // given
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characteristicsRequest)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getCharacteristics_should_return_400_when_non_uuid_gameSaveId() {
        // when
        mockMvc.perform(get("/api/v1/characteristics/{gameSaveId}", "testtesttest"))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getCharacteristics_should_return_200_when_authenticated_user_and_valid_uuid() {
        // when
        mockMvc.perform(get("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCharacteristics_should_return_400_when_no_body() {
        // when
        mockMvc.perform(post("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCharacteristics_should_return_400_when_body_is_null() {
        // when
        mockMvc.perform(post("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCharacteristics_should_return_400_when_gameSaveId_is_non_uuid() {
        // given
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/characteristics/{gameSaveId}", "testtesttest")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characteristicsRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCharacteristics_should_return_400_when_one_CharacteristicsRequest_field_is_negative() {
        // given
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(-1L, 1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characteristicsRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCharacteristics_should_return_400_when_one_CharacteristicsRequest_field_is_zero() {
        // given
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 0L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characteristicsRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCharacteristics_should_return_200_if_one_CharacteristicsRequest_field_is_null() {
        // given
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(null, 1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characteristicsRequest)))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCharacteristics_should_return_200_when_authenticated_user_valid_body_and_vlaid_gameSaveId() {
        // given
        CharacteristicsRequest characteristicsRequest = new CharacteristicsRequest(1L, 1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/characteristics/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characteristicsRequest)))
                // then
                .andExpect(status().isOk());
    }
}
