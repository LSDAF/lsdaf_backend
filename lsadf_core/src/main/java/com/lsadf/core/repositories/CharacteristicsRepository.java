package com.lsadf.core.repositories;

import com.lsadf.core.entities.CharacteristicsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Repository class for CharacteristicsEntity */
@Repository
public interface CharacteristicsRepository extends CrudRepository<CharacteristicsEntity, String> {}
