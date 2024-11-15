package com.lsadf.lsadf_backend.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.lsadf_backend.controllers.InventoryController;
import com.lsadf.core.controllers.advices.GlobalExceptionHandler;
import com.lsadf.lsadf_backend.controllers.impl.InventoryControllerImpl;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({InventoryController.class, InventoryControllerImpl.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getInventory_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/inventory/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getInventory_should_return_400_when_non_uuid_gameSaveId() {
        // when
        mockMvc.perform(get("/api/v1/inventory/{gameSaveId}", "testtesttest"))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getInventory_should_return_200_when_authenticated_user_and_valid_uuid() {
        // when
        mockMvc.perform(get("/api/v1/inventory/{gameSaveId}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isOk());
    }
}
