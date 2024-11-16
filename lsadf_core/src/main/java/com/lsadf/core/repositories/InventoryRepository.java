package com.lsadf.core.repositories;

import com.lsadf.core.entities.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, String>, JpaSpecificationExecutor<InventoryEntity> {
}
