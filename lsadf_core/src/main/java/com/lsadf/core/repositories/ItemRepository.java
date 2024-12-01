package com.lsadf.core.repositories;

import com.lsadf.core.entities.ItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, String> {}
