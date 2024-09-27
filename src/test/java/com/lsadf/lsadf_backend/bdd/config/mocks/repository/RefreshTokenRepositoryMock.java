package com.lsadf.lsadf_backend.bdd.config.mocks.repository;

import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.TokenStatus;
import com.lsadf.lsadf_backend.repositories.RefreshTokenRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.ClockService;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class RefreshTokenRepositoryMock extends ARepositoryMock<RefreshTokenEntity> implements RefreshTokenRepository {

    private final UserRepository userRepository;
    private final ClockService clockService;

    public RefreshTokenRepositoryMock(UserRepository userRepository,
                                      ClockService clockService) {
        this.userRepository = userRepository;
        this.clockService = clockService;
    }


    @Override
    public Optional<RefreshTokenEntity> findByUserAndStatus(UserEntity user, TokenStatus status) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getUser().equals(user) && entity.getStatus().equals(status))
                .findFirst();
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getToken().equals(token))
                .findFirst();
    }

    /**
     * Find all refresh tokens by status
     *
     * @param status the status
     * @return the list of refresh tokens
     */
    public Iterable<RefreshTokenEntity> findAllByStatus(TokenStatus status) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getStatus().equals(status))
                .toList();
    }

    /**
     * Find all refresh tokens by expiration date
     *
     * @param date the date
     * @return the list of refresh tokens
     */
    @Override
    public Stream<RefreshTokenEntity> findAllByExpirationDateBefore(Date date) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getExpirationDate().before(date));
    }

    /**
     * @param entity the entity
     * @param <S>    the entity type
     * @return the saved entity
     */
    @Override
    public <S extends RefreshTokenEntity> @NotNull S save(S entity) {
        Date now = clockService.nowDate();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }


        RefreshTokenEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            entities.put(entity.getId(), entity);
            return entity;
        }

        toUpdate.setUpdatedAt(now);
        if (entity.getStatus() != null) {
            toUpdate.setStatus(entity.getStatus());
        }
        if (entity.getInvalidationDate() != null) {
            toUpdate.setInvalidationDate(entity.getInvalidationDate());
        }
        if (entity.getExpirationDate() != null) {
            toUpdate.setExpirationDate(entity.getExpirationDate());
        }

        entities.put(entity.getId(), toUpdate);

        return (S) toUpdate;
    }
}
