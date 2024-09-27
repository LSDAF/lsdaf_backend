package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.models.TokenStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtTokenEntity, String> {
    Optional<JwtTokenEntity> findByToken(String token);

    @Query("select j from t_jwt_token j where j.status = :status and j.expirationDate > :date")
    @Transactional(readOnly = true)
    Stream<JwtTokenEntity> findAllByStatusAndExpirationDateAfter(TokenStatus status, Date date);

    @Query("select j from t_jwt_token j where j.expirationDate < :date")
    @Transactional
    Stream<JwtTokenEntity> findAllByExpirationDateBefore(Date date);
}
