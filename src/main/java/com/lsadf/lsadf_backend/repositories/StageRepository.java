package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.StageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends CrudRepository<StageEntity, String> {
}
