package com.lsadf.core.repositories;

import com.lsadf.core.entities.StageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRepository extends JpaRepository<StageEntity, String>, JpaSpecificationExecutor<StageEntity> {
}
