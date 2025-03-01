package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameObjectRepository extends JpaRepository<GameObject, Long> {
}
