package com.lsadf.lsadf_backend.bdd.config.mocks;

import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Mock implementation of the UserRepository
 */
public class UserRepositoryMock extends ARepositoryMock<UserEntity> implements UserRepository {

    @Override
    public <S extends UserEntity> S save(S entity) {
        Date now = new Date();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }

        UserEntity toUpdate = entities.get(entity.getId());
        if (toUpdate == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            entities.put(entity.getId(), entity);
            return entity;
        }
        toUpdate.setName(entity.getName());
        toUpdate.setEmail(entity.getEmail());
        toUpdate.setPassword(entity.getPassword());
        toUpdate.setUpdatedAt(now);
        entities.put(entity.getId(), toUpdate);
        return (S) toUpdate;
    }

    @Override
    public boolean existsByEmail(String email) {
        return entities.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public Optional<UserEntity> findUserEntityByEmail(String email) {
        Optional<UserEntity> userEntityOptional = entities.values()
                .stream()
                .filter(user -> {
                    String userEmail = user.getEmail();
                    return email.equals(userEmail);
                })
                .findFirst();

        return userEntityOptional;
    }

    public void deleteUserEntityByEmail(String email) {
        entities.values()
                .removeIf(user -> user.getEmail().equals(email));
    }

    public Stream<UserEntity> findAllUsers() {
        return entities.values().stream();
    }
}
