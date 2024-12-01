package com.lsadf.core.repositories;

import com.lsadf.core.entities.GameSaveEntity;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Repository class for GameSaveEntity */
@Repository
public interface GameSaveRepository extends CrudRepository<GameSaveEntity, String> {
  @Query("select gs from t_game_save gs")
  Stream<GameSaveEntity> findAllGameSaves();

  Optional<GameSaveEntity> findGameSaveEntityByNickname(String nickname);

  @Query("select gs from t_game_save gs where gs.userEmail = :userId")
  Stream<GameSaveEntity> findGameSaveEntitiesByUserEmail(String userId);
}
