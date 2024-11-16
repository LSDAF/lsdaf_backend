package com.lsadf.core.repositories;

import com.lsadf.core.entities.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String>, JpaSpecificationExecutor<CurrencyEntity> {
}
