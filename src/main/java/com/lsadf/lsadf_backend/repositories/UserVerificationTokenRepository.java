package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.tokens.UserVerificationTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVerificationTokenRepository extends CrudRepository<UserVerificationTokenEntity, String> {
    Optional<UserVerificationTokenEntity> findUserValidationTokenEntityByToken(String token);

    Iterable<UserVerificationTokenEntity> findAllByUserEmail(String email);
}
