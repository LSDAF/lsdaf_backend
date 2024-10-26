package com.lsadf.lsadf_backend.unit.controllers.admin;

import com.lsadf.lsadf_backend.controllers.admin.AdminGlobalInfoController;
import com.lsadf.lsadf_backend.controllers.admin.impl.AdminGlobalInfoControllerImpl;
import com.lsadf.lsadf_backend.controllers.exception_handler.GlobalExceptionHandler;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {GlobalExceptionHandler.class, AdminGlobalInfoController.class, AdminGlobalInfoControllerImpl.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AdminGlobalInfoControllerTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @SneakyThrows
    void getGlobalInfo_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/admin/global_info")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getGlobalInfo_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/global_info")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getGlobalInfo_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/global_info")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isOk());
    }
}
