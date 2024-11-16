package com.lsadf.core.repositories;

import com.lsadf.core.entities.CharacteristicsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository class for CharacteristicsEntity
 */
@Repository
public interface CharacteristicsRepository extends JpaRepository<CharacteristicsEntity, String>, JpaSpecificationExecutor<CharacteristicsEntity> {
}
