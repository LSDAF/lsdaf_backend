package com.lsadf.lsadf_backend.unit.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.core.controllers.advices.GlobalExceptionHandler;
import com.lsadf.core.requests.user.UserLoginRequest;
import com.lsadf.core.requests.user.UserRefreshLoginRequest;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.lsadf_backend.controllers.AuthController;
import com.lsadf.lsadf_backend.controllers.impl.AuthControllerImpl;
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

@RunWith(SpringRunner.class)
@WebMvcTest(value = {AuthControllerImpl.class, AuthController.class, GlobalExceptionHandler.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class AuthControllerTests {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void should_return_400_when_login_request_contains_null_username() {
    // given
    UserLoginRequest loginRequest = new UserLoginRequest(null, "password");
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void should_return_400_when_login_request_contains_null_password() {
    // given
    UserLoginRequest loginRequest = new UserLoginRequest("test@test.com", null);
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void should_return_400_when_login_request_contains_not_email_username() {
    // given
    UserLoginRequest loginRequest = new UserLoginRequest("username", "password");
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void should_return_400_when_login_request_is_null() {
    // given
    UserLoginRequest loginRequest = null;
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  @SneakyThrows
  void should_return_400_when_refresh_request_contains_null_refresh_token() {
    // given
    UserRefreshLoginRequest refreshRequest = new UserRefreshLoginRequest(null);
    // when
    mockMvc
        .perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(refreshRequest))
                .accept(MediaType.APPLICATION_JSON_VALUE))
        // then
        .andExpect(status().isBadRequest());
  }
}
