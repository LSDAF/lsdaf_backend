package com.lsadf.lsadf_backend.bdd.config.mocks.repository;

import com.lsadf.lsadf_backend.entities.tokens.UserVerificationTokenEntity;
import com.lsadf.lsadf_backend.repositories.UserVerificationTokenRepository;
import com.lsadf.lsadf_backend.services.ClockService;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class UserVerificationTokenRepositoryMock extends ARepositoryMock<UserVerificationTokenEntity> implements UserVerificationTokenRepository {

    private final ClockService clockService;

    public UserVerificationTokenRepositoryMock(ClockService clockService) {
        this.clockService = clockService;
    }

    @Override
    public <S extends UserVerificationTokenEntity> @NotNull S save(@NotNull S entity) {
        Date now = clockService.nowDate();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        UserVerificationTokenEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            entities.put(entity.getId(), entity);
            return entity;
        }

        toUpdate.setUpdatedAt(now);

        if (toUpdate.getStatus() != entity.getStatus()) {
            toUpdate.setStatus(entity.getStatus());
        }

        entities.put(entity.getId(), toUpdate);

        return (S) toUpdate;
    }

    @Override
    public Optional<UserVerificationTokenEntity> findUserValidationTokenEntityByToken(String token) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getToken().equals(token))
                .findFirst();
    }

    @Override
    public Iterable<UserVerificationTokenEntity> findAllByUserEmail(String email) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getUser().getEmail().equals(email))
                .toList();
    }
}
