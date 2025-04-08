package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface GameObjectRepository extends JpaRepository<GameObject, Long> {

    @Modifying
    @Query(value = "DELETE FROM game_objects g WHERE g.author = :id", nativeQuery = true)
    void deleteAllUserGameObjects(@Param("id") Long id);
}
