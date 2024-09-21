package com.lsadf.lsadf_backend.bdd.config.mocks.impl;

import com.lsadf.lsadf_backend.bdd.config.mocks.RepositoryMock;
import com.lsadf.lsadf_backend.entities.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.repositories.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class RefreshTokenRepositoryMock extends ARepositoryMock<RefreshTokenEntity> implements RepositoryMock<RefreshTokenEntity> {

    private final UserRepository userRepository;

    public RefreshTokenRepositoryMock(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Find a refresh token by user email and status
     *
     * @param user the user
     * @param status    the status
     * @return the refresh token
     */
    public Optional<RefreshTokenEntity> findByUserEmailAndStatus(UserEntity user, RefreshTokenEntity.Status status) {
        return entities.values()
                .stream()
                .filter(refreshToken -> refreshToken.getUser().equals(user) && refreshToken.getStatus().equals(status))
                .findFirst();
    }

    /**
     * Find a refresh token by token
     *
     * @param token the token
     * @return the refresh token
     */
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
    public Iterable<RefreshTokenEntity> findAllByStatus(RefreshTokenEntity.Status status) {
        return entities.values()
                .stream()
                .filter(entity -> entity.getStatus().equals(status))
                .toList();
    }

    /**
     * Save a refresh token
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    @Override
    public RefreshTokenEntity save(RefreshTokenEntity entity) {
        Date now = new Date();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        if (entity.getUser() == null) {

        }

        RefreshTokenEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
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

        return toUpdate;
    }
}
