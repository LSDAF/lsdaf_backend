package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for BankAccountEntity
 */
@Repository
public interface GameSaveRepository extends CrudRepository<GameSaveEntity, String> {
}
