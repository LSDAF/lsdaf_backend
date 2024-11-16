package com.lsadf.core.repositories;

import com.lsadf.core.entities.GameSaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository class for GameSaveEntity
 */
@Repository
public interface GameSaveRepository extends JpaRepository<GameSaveEntity, String>, JpaSpecificationExecutor<GameSaveEntity> {
    @Query("select gs from t_game_save gs")
    Stream<GameSaveEntity> findAllGameSaves();

    Optional<GameSaveEntity> findGameSaveEntityByNickname(String nickname);

    @Query("select gs from t_game_save gs where gs.userEmail = :userId")
    Stream<GameSaveEntity> findGameSaveEntitiesByUserEmail(String userId);

}
