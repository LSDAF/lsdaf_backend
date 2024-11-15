package com.lsadf.lsadf_backend.unit.controllers;

import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import com.lsadf.lsadf_backend.controllers.UserController;
import com.lsadf.core.controllers.advices.GlobalExceptionHandler;
import com.lsadf.lsadf_backend.controllers.impl.UserControllerImpl;
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
@WebMvcTest({GlobalExceptionHandler.class, UserController.class, UserControllerImpl.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @SneakyThrows
    void getUserInfo_should_return_200_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/user/me"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getUserInfo_should_return_200_when_user_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/user/me"))
                // then
                .andExpect(status().isOk());
    }
}
