package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.CurrencyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<CurrencyEntity, String> {
}
