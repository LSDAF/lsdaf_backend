package com.lsadf.admin.unit.controllers;

import com.lsadf.admin.controllers.AdminGlobalInfoController;
import com.lsadf.admin.controllers.impl.AdminGlobalInfoControllerImpl;
import com.lsadf.core.controllers.advices.GlobalExceptionHandler;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(
    value = {
      GlobalExceptionHandler.class,
      AdminGlobalInfoController.class,
      AdminGlobalInfoControllerImpl.class
    })
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class AdminGlobalInfoControllerTests {

  @Autowired private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void getGlobalInfo_should_return_401_when_user_not_authenticated() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/global_info")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
  void getGlobalInfo_should_return_403_when_user_not_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/global_info")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockJwtUser(
      username = "paul.ochon@test.com",
      name = "Paul OCHON",
      roles = {"ADMIN"})
  void getGlobalInfo_should_return_200_when_authenticated_user_is_admin() {
    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/v1/admin/global_info")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
