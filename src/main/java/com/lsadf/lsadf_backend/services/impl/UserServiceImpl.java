package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.http_clients.KeycloakAdminClient;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.properties.KeycloakProperties;
import com.lsadf.lsadf_backend.requests.admin.AdminUserCreationRequest;
import com.lsadf.lsadf_backend.requests.admin.AdminUserUpdateRequest;
import com.lsadf.lsadf_backend.requests.user.UserCreationRequest;
import com.lsadf.lsadf_backend.requests.user.UserUpdateRequest;
import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
public class UserServiceImpl implements UserService {

    private final KeycloakAdminClient keycloakAdminClient;
    private final KeycloakProperties keycloakProperties;
    private final ClockService clockService;

    private final String realm;

    public UserServiceImpl(KeycloakProperties keycloakProperties,
                           KeycloakAdminClient keycloakAdminClient,
                           ClockService clockService) {
        this.keycloakAdminClient = keycloakAdminClient;
        this.keycloakProperties = keycloakProperties;
        this.clockService = clockService;

        this.realm = keycloakProperties.getRealm();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<User> getUsers() {
        return keycloakAdminClient.getUsers(realm).stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<User> getUsers(String search) {
        return keycloakAdminClient.getUsers(realm, Optional.ofNullable(search)).stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return keycloakAdminClient.getUserById(realm, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        return keycloakAdminClient.getUserByUsername(realm, username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(String id, AdminUserUpdateRequest adminUpdateRequest) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        User existingUser = keycloakAdminClient.getUserById(realm, id);
        if (adminUpdateRequest.getFirstName() != null) {
            existingUser.setFirstName(adminUpdateRequest.getFirstName());
        }
        if (adminUpdateRequest.getLastName() != null) {
            existingUser.setLastName(adminUpdateRequest.getLastName());
        }
        if (adminUpdateRequest.getEmailVerified() != null) {
            existingUser.setEmailVerified(adminUpdateRequest.getEmailVerified());
        }
        if (adminUpdateRequest.getEnabled() != null) {
            existingUser.setEnabled(adminUpdateRequest.getEnabled());
        }
        if (adminUpdateRequest.getUserRoles() != null) {
            existingUser.setUserRoles(adminUpdateRequest.getUserRoles());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(String id, UserUpdateRequest updateRequest) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        User existingUser = keycloakAdminClient.getUserById(realm, id);
        if (updateRequest.getFirstName() != null) {
            existingUser.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            existingUser.setLastName(updateRequest.getLastName());
        }
        keycloakAdminClient.updateUser(realm, id, existingUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetUserPassword(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        String updatePasswordAction = "UPDATE_PASSWORD";
        List<String> actions = List.of(updatePasswordAction);
        keycloakAdminClient.sendActionsEmail(realm, id, actions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        keycloakAdminClient.deleteUser(realm, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(UserCreationRequest request) {
        User newUser = User
                .builder()
                .id(UUID.randomUUID().toString())
                .userRoles(request.getUserRoles())
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailVerified(false)
                .enabled(true)
                .creationTimestamp(clockService.nowDate())
                .build();

        return keycloakAdminClient.createUser(realm, newUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(AdminUserCreationRequest adminUserCreationRequest) {
        User newUser = User
                .builder()
                .id(UUID.randomUUID().toString())
                .username(adminUserCreationRequest.getUsername())
                .firstName(adminUserCreationRequest.getFirstName())
                .lastName(adminUserCreationRequest.getLastName())
                .emailVerified(adminUserCreationRequest.getEmailVerified())
                .enabled(adminUserCreationRequest.getEnabled())
                .creationTimestamp(clockService.nowDate())
                .build();

        return keycloakAdminClient.createUser(realm, newUser);
    }
}
