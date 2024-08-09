package com.lsadf.lsadf_backend.bdd.config.mocks;

import com.lsadf.lsadf_backend.entities.UserEntity;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class UserRepositoryMock extends ARepositoryMock<UserEntity> {
    @Override
    public UserEntity save(UserEntity entity) {
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
        return toUpdate;
    }

    public Optional<UserEntity> findUserEntityByEmail(String email) {
        var userEntityOptional = entities.values()
                .stream()
                .filter(user -> {
                    var userEmail = user.getEmail();
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
