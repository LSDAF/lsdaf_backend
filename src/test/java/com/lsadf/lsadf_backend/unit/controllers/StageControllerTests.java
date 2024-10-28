package com.lsadf.lsadf_backend.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.lsadf_backend.controllers.StageController;
import com.lsadf.lsadf_backend.controllers.exception_handler.GlobalExceptionHandler;
import com.lsadf.lsadf_backend.controllers.impl.StageControllerImpl;
import com.lsadf.lsadf_backend.requests.stage.StageRequest;
import com.lsadf.lsadf_backend.unit.config.UnitTestConfiguration;
import com.lsadf.lsadf_backend.unit.config.WithMockJwtUser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({StageControllerImpl.class, StageController.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class StageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @SneakyThrows
    void getStage_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getStage_should_return_400_when_non_uuid_gameSaveId() {
        // when
        mockMvc.perform(get("/api/v1/stage/{gameSaveId}", "testtesttest"))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getStage_should_return_200_when_authenticated_user_and_valid_uuid() {
        // when
        mockMvc.perform(get("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveStage_should_return_400_when_no_body() {
        // when
        mockMvc.perform(post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveStage_should_return_400_when_body_is_null() {
        // when
        mockMvc.perform(post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveStage_should_return_400_when_gameSaveId_is_non_uuid() {
        // given
        StageRequest stageRequest = new StageRequest(5L, 2L);
        // when
        mockMvc.perform(post("/api/v1/stage/{gameSaveId}", "testtesttest")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stageRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveStage_should_return_400_when_max_stage_smaller_than_current_stage() {
        // given
        StageRequest stageRequest = new StageRequest(5L, 2L);

        // when
        mockMvc.perform(post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stageRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveStage_should_return_400_when_stages_negative() {
        // given
        StageRequest stageRequest = new StageRequest(-5L, -2L);

        // when
        mockMvc.perform(post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stageRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void saveStage_should_return_401_when_user_not_authenticated() {
        // given
        StageRequest stageRequest = new StageRequest(10L, 25L);
        // when
        mockMvc.perform(post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stageRequest)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveStage_should_return_200_when_valid_body_valid_gameSaveId_and_authenticated_user() {
        // given
        StageRequest stageRequest = new StageRequest(10L, 25L);
        // when
        mockMvc.perform(post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stageRequest)))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveStage_should_return_200_if_one_stageRequest_field_is_null() {
        // given
        StageRequest stageRequest1 = new StageRequest(125L, null);
        // when
        mockMvc.perform(post("/api/v1/stage/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stageRequest1)))
                // then
                .andExpect(status().isOk());
    }
}