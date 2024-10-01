package com.lsadf.lsadf_backend.repositories;

import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository class for GameSaveEntity
 */
@Repository
public interface GameSaveRepository extends CrudRepository<GameSaveEntity, String> {
    @Query("select gs from t_game_save gs")
    Stream<GameSaveEntity> findAllGameSaves();

    Optional<GameSaveEntity> findGameSaveEntityByNickname(String nickname);

    List<GameSaveEntity> findGameSaveEntitiesByUserEmail(String userId);
}
