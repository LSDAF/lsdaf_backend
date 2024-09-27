package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.models.TokenStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtTokenEntity, String> {
    Optional<JwtTokenEntity> findByToken(String token);
    Iterable<JwtTokenEntity> findAllByStatusAndExpirationDateAfter(TokenStatus status, Date date);
    Iterable<JwtTokenEntity> findAllByExpirationDateBefore(Date date);
}
