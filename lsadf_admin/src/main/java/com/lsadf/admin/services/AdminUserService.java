package com.lsadf.admin.services;

import com.lsadf.core.http_clients.KeycloakAdminClient;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.User;
import com.lsadf.core.properties.KeycloakProperties;
import com.lsadf.core.services.ClockService;
import com.lsadf.core.services.UserService;
import com.lsadf.core.services.impl.UserServiceImpl;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import com.vaadin.hilla.Nullable;
import com.vaadin.hilla.crud.CrudService;
import com.vaadin.hilla.crud.filter.Filter;
import lombok.NonNull;
import org.keycloak.admin.client.Keycloak;
import org.springframework.data.domain.Pageable;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class AdminUserService extends UserServiceImpl implements CrudService<User, String>, UserService {

    public AdminUserService(Keycloak keycloak, KeycloakProperties keycloakProperties, KeycloakAdminClient keycloakAdminClient, ClockService clockService, Mapper mapper) {
        super(keycloak, keycloakProperties, keycloakAdminClient, clockService, mapper);
    }

    @Override
    public @Nullable User save(@NonNull User user) {
        if (this.checkIdExists(user.getId())) {
            var userCreationRequest = mapper.mapUserToAdminUserUpdateRequest(user);
            return this.updateUser(user.getId(), userCreationRequest);
        }

        var adminUserCreationRequest = mapper.mapUserToAdminUserCreationRequest(user);
        return this.createUser(adminUserCreationRequest);
    }

    @Override
    public void delete(@NonNull String id) {
        this.deleteUser(id);
    }

    @Override
    public @NonNull List<@Nonnull User> list(@NonNull Pageable pageable, @Nullable Filter filter) {
        var results = getUsers().skip(pageable.getOffset()).limit(pageable.getPageSize()).toList();
        results.forEach(user -> {
            // TODO use filters
        });

        return results;
    }
}
