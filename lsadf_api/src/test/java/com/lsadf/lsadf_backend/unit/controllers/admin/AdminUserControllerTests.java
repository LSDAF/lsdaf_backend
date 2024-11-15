package com.lsadf.lsadf_backend.unit.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.lsadf_backend.controllers.admin.AdminUserController;
import com.lsadf.lsadf_backend.controllers.admin.impl.AdminUserControllerImpl;
import com.lsadf.lsadf_backend.controllers.advices.GlobalExceptionHandler;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static com.lsadf.core.constants.ControllerConstants.Params.ORDER_BY;
import static com.lsadf.lsadf_backend.requests.user.UserOrderBy.FIRST_NAME_DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {GlobalExceptionHandler.class, AdminUserController.class, AdminUserControllerImpl.class})
@Import(UnitTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class AdminUserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void updateUser_should_return_401_when_user_not_authenticated() {
        // given
        AdminUserUpdateRequest request = AdminUserUpdateRequest.builder()
                .firstName("Paul")
                .lastName("OCHON")
                .userRoles(List.of("ADMIN"))
                .enabled(true)
                .emailVerified(true)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void updateUser_should_return_403_when_user_not_admin() {
        // given
        AdminUserUpdateRequest request = AdminUserUpdateRequest.builder()
                .firstName("Paul")
                .lastName("OCHON")
                .userRoles(List.of("ADMIN"))
                .enabled(true)
                .emailVerified(true)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateUser_should_return_400_when_id_is_not_uuid() {
        // given
        AdminUserUpdateRequest request = AdminUserUpdateRequest.builder()
                .firstName("Paul")
                .lastName("OCHON")
                .userRoles(List.of("ADMIN"))
                .enabled(true)
                .emailVerified(true)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/users/id/{user_id}", "testtesttest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateUser_should_return_200_when_authenticated_user_is_admin() {
        // given
        AdminUserUpdateRequest request = AdminUserUpdateRequest.builder()
                .firstName("Paul")
                .lastName("OCHON")
                .userRoles(List.of("ADMIN"))
                .enabled(true)
                .emailVerified(true)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> updateUserArgumentsProvider() {
        return Stream.of(
                Arguments.of("", "OCHON", true, true), // invalid first name
                Arguments.of(null, "OCHON", true, true), // invalid first name 2
                Arguments.of("Paul", "", true, true), // invalid last name
                Arguments.of("Paul", null, true, true), // invalid last name 2
                Arguments.of("Paul", "OCHON", null, true), // invalid enabled
                Arguments.of("Paul", "OCHON", true, null) // invalid email verified
        );
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("updateUserArgumentsProvider")
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void updateUser_should_return_400_when_request_is_invalid(String firstName,
                                                              String lastName,
                                                              Boolean enabled,
                                                              Boolean emailVerified) {
        // given
        AdminUserUpdateRequest request = AdminUserUpdateRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .userRoles(List.of("ADMIN"))
                .enabled(enabled)
                .emailVerified(emailVerified)
                .build();
        // when
        mockMvc.perform(post("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void createUser_should_return_401_when_user_not_authenticated() {
        // given
        AdminUserCreationRequest request = AdminUserCreationRequest.builder()
                .firstName("Paul")
                .lastName("ITESSE")
                .username("paul.itesse@test.com")
                .enabled(true)
                .emailVerified(true)
                .build();

        // when
        mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void createUser_should_return_403_when_user_not_admin() {
        // given
        AdminUserCreationRequest request = AdminUserCreationRequest.builder()
                .firstName("Paul")
                .lastName("ITESSE")
                .username("paul.itesse@test.com")
                .enabled(true)
                .emailVerified(true)
                .build();

        // when
        mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void createUser_should_return_200_when_authenticated_user_is_admin() {
        // given
        AdminUserCreationRequest request = AdminUserCreationRequest.builder()
                .firstName("Paul")
                .lastName("ITESSE")
                .username("paul.itesse@test.com")
                .enabled(true)
                .emailVerified(true)
                .build();

        // when
        mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> createUserArgumentsProvider() {
        return Stream.of(
                Arguments.of("paul.itesse", "Paul", "ITESSE", true, true), // invalid username
                Arguments.of("paul.itesse@test.com", "", "ITESSE", true, true), // invalid first name
                Arguments.of("paul.itesse@test.com", null, "ITESSE", true, true), // invalid first name 2
                Arguments.of("paul.itesse@test.com", "Paul", "", true, true), // invalid last name
                Arguments.of("paul.itesse@test.com", "Paul", null, true, true), // invalid last name 2
                Arguments.of("paul.itesse@test.com", "Paul", "ITESSE", null, true), // invalid enabled
                Arguments.of("paul.itesse@test.com", "Paul", "ITESSE", true, null) // invalid email verified
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    @MethodSource("createUserArgumentsProvider")
    void createUser_should_return_400_when_request_is_invalid(String username,
                                                              String firstName,
                                                              String lastName,
                                                              Boolean enabled,
                                                              Boolean emailVerified) {
        // given
        AdminUserCreationRequest request = AdminUserCreationRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .enabled(enabled)
                .emailVerified(emailVerified)
                .build();

        // when
        mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void deleteUser_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(delete("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void deleteUser_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(delete("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void deleteUser_should_return_400_when_id_is_not_uuid() {
        // when
        mockMvc.perform(delete("/api/v1/admin/users/id/{user_id}", "testtesttest"))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void deleteUser_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(delete("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef"))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getUserByUsername_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/admin/users/username/{username}", "paul.ochon@test.com")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getUserByUsername_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/users/username/{username}", "paul.ochon@test.com")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getUserByUsername_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/users/username/{username}", "test@test.com")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getUserByUsername_should_return_400_when_username_is_not_valid() {
        // when
        mockMvc.perform(get("/api/v1/admin/users/username/{username}", "test")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getUserById_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isUnauthorized());
    }


    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getUserById_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getUserById_should_return_200_when_authenticated_user_is_admin_and_valid_uuid() {
        // when
        mockMvc.perform(get("/api/v1/admin/users/id/{user_id}", "36f27c2a-06e8-4bdb-bf59-56999116f5ef")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getUserByid_should_return_400_when_id_is_not_uuid() {
        // when
        mockMvc.perform(get("/api/v1/admin/users/id/{user_id}", "testtesttest")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getUsers_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void getUsers_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getUsers_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getUsers_should_return_400_when_invalid_order_by() {
        // when
        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param(ORDER_BY, "INVALID_ORDER_BY"))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void getUsers_should_return_200_when_order_by_is_set() {
        // when
        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param(ORDER_BY, FIRST_NAME_DESC.name()))
                // then
                .andExpect(status().isOk());
    }

}
