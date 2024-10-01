package com.lsadf.lsadf_backend.unit;

import com.lsadf.lsadf_backend.constants.UserRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the UserRole class to test static methods
 */
class UserRoleTests {

    @Test
    void should_return_good_role() {
        // Given the following string role

        String role = "ROLE_USER";

        // When I call UserRole.fromString with the string role

        UserRole result = UserRole.fromRole(role);

        // Then I should get the correct UserRole

        assertThat(result).isEqualTo(UserRole.USER);

    }

    @Test
    void should_return_null_for_wrong_role() {
        // Given the following string role

        String role = "ROLE_SYS_MODO";

        // When I call UserRole.fromString with the string role

        UserRole result = UserRole.fromRole(role);

        // Then I should get null

        assertThat(result).isNull();
    }

    @Test
    void should_return_good_name() {
        // Given the following name

        String name = "USER";

        // When I call UserRole.fromName with the name

        UserRole result = UserRole.fromName(name);

        // Then I should get the correct UserRole

        assertThat(result).isEqualTo(UserRole.USER);
    }

    @Test
    void should_return_null_for_wrong_name() {
        // Given the following name

        String name = "ROLE_USER";

        // When I call UserRole.fromName with the name

        UserRole result = UserRole.fromName(name);

        // Then I should get null

        assertThat(result).isNull();
    }
}
