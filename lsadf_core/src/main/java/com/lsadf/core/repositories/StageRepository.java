package com.lsadf.core.repositories;

import com.lsadf.core.entities.StageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends CrudRepository<StageEntity, String> {
}
