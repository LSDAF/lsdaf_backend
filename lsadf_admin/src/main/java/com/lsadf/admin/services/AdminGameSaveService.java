package com.lsadf.admin.services;

import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.models.GameSave;
import com.lsadf.core.services.GameSaveService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import com.vaadin.hilla.Nullable;
import com.vaadin.hilla.crud.CrudService;
import com.vaadin.hilla.crud.filter.Filter;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class AdminGameSaveService implements CrudService<GameSave, String> {

    private final GameSaveService gameSaveService;
    private final Mapper mapper;

    public AdminGameSaveService(GameSaveService gameSaveService,
                                Mapper mapper) {
        this.gameSaveService = gameSaveService;
        this.mapper = mapper;
    }

    @Override
    public @Nullable GameSave save(@NonNull GameSave gameSave) {
        return gameSave;
    }

    @Override
    @Transactional
    public void delete(@NonNull String gameSaveId) {
        gameSaveService.deleteGameSave(gameSaveId);
    }

    @Override
    public @NonNull List<@Nonnull GameSave> list(Pageable pageable, @Nullable Filter filter) {
        var stream = gameSaveService.getGameSaves();
        return stream.skip(pageable.getOffset()).limit(pageable.getPageSize()).map(mapper::mapGameSaveEntityToGameSave).toList();
    }
}
