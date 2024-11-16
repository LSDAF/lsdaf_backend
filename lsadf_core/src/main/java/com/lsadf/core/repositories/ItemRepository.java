package com.lsadf.core.repositories;

import com.lsadf.core.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, String>, JpaSpecificationExecutor<ItemEntity> {
}
