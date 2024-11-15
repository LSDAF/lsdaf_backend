package com.lsadf.lsadf_backend.repositories;

import com.lsadf.core.entities.InventoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends CrudRepository<InventoryEntity, String> {
}
