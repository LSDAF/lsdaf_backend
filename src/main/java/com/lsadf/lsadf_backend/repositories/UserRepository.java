package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository class for UserEntity
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    Optional<UserEntity> findUserEntityByEmail(String email);
    void deleteUserEntityByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = "SELECT * from T_USER", nativeQuery = true)
    Stream<UserEntity> findAllUsers();
}
