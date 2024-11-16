package com.lsadf.admin.services;

import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.User;
import com.lsadf.core.services.UserService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import com.vaadin.hilla.Nullable;
import com.vaadin.hilla.crud.CrudService;
import com.vaadin.hilla.crud.filter.Filter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class AdminUserService implements CrudService<User, String> {
    private final UserService userService;
    private final Mapper mapper;

    public AdminUserService(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public @Nullable User save(User value) {
        return value;
    }

    @Override
    public void delete(String s) {
        userService.deleteUser(s);
    }

    @Override
    public @Nonnull List<@Nonnull User> list(Pageable pageable, @Nullable Filter filter) {
        return List.of();
    }
}
