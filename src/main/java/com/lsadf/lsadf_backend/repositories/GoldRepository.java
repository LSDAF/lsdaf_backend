package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.GoldEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldRepository extends CrudRepository<GoldEntity, String> {
}
