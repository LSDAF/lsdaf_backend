package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.models.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for UserEntity
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    Optional<UserEntity> findUserEntityByEmail(String email);
    void deleteUserEntityByEmail(String email);
}
