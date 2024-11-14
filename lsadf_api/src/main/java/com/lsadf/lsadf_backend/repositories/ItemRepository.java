package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.ItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository  extends CrudRepository<ItemEntity, String> {
}
