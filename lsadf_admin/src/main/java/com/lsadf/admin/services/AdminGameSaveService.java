package com.lsadf.admin.services;

import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.GameSave;
import com.lsadf.core.services.GameSaveService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudService;
import com.vaadin.hilla.crud.filter.Filter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class AdminGameSaveService implements CrudService<GameSave, String> {

    private final GameSaveService gameSaveService;
    private final Mapper mapper;

    public AdminGameSaveService(GameSaveService gameSaveService, Mapper mapper) {
        this.gameSaveService = gameSaveService;
        this.mapper = mapper;
    }

    @Override
    public GameSave save(GameSave value) {
        return null;
    }

    @Override
    public void delete(String s) {
    }

    @Override
    public List<GameSave> list(Pageable pageable, Filter filter) {
        return List.of();
    }
}
