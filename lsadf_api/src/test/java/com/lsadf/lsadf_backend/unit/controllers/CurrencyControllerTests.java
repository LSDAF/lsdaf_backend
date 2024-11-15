package com.lsadf.lsadf_backend.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.lsadf_backend.controllers.CurrencyController;
import com.lsadf.lsadf_backend.controllers.advices.GlobalExceptionHandler;
import com.lsadf.lsadf_backend.controllers.impl.CurrencyControllerImpl;
import com.lsadf.core.requests.currency.CurrencyRequest;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({CurrencyControllerImpl.class, CurrencyController.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class CurrencyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getCurrency_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void saveCurrency_should_return_401_when_user_not_authenticated() {
        // given
        CurrencyRequest currencyRequest = new CurrencyRequest(1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyRequest)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getCurrency_should_return_400_when_non_uuid_gameSaveId() {
        // when
        mockMvc.perform(get("/api/v1/currency/{gameSaveId}", "testtesttest"))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getCurrency_should_return_200_when_authenticated_user_and_valid_uuid() {
        // when
        mockMvc.perform(get("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCurrency_should_return_400_when_no_body() {
        // when
        mockMvc.perform(post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCurrency_should_return_400_when_body_is_null() {
        // when
        mockMvc.perform(post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCurrency_should_return_400_when_gameSaveId_is_non_uuid() {
        // given
        CurrencyRequest currencyRequest = new CurrencyRequest(1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/currency/{gameSaveId}", "testtesttest")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCurrency_should_return_400_when_one_CurrencyRequest_field_is_negative() {
        // given
        CurrencyRequest currencyRequest = new CurrencyRequest(-1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCurrency_should_return_200_if_one_CurrencyRequest_field_is_null() {
        // given
        CurrencyRequest currencyRequest = new CurrencyRequest(null, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyRequest)))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void saveCurrency_should_return_200_when_authenticated_user_valid_body_and_vlaid_gameSaveId() {
        // given
        CurrencyRequest currencyRequest = new CurrencyRequest(1L, 1L, 1L, 1L);
        // when
        mockMvc.perform(post("/api/v1/currency/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currencyRequest)))
                // then
                .andExpect(status().isOk());
    }
}
