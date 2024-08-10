package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Repository class for GameSaveEntity
 */
@Repository
public interface GameSaveRepository extends CrudRepository<GameSaveEntity, String> {
    @Query(value = "SELECT * from T_GAME_SAVE", nativeQuery = true)
    Stream<GameSaveEntity> findAllGameSaves();
}
