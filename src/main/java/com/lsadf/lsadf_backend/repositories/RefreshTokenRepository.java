package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByUserAndStatus(UserEntity user, RefreshTokenEntity.Status status);
    Optional<RefreshTokenEntity> findByToken(String token);
    Iterable<RefreshTokenEntity> findAllByStatus(RefreshTokenEntity.Status status);
}
