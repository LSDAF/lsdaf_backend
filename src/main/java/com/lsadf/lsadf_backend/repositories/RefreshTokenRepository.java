package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.TokenStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByUserAndStatus(UserEntity user, TokenStatus status);
    Optional<RefreshTokenEntity> findByToken(String token);

    @Query("select r from t_refresh_token r where r.expirationDate < :date")
    Stream<RefreshTokenEntity> findAllByExpirationDateBefore(Date date);
}
