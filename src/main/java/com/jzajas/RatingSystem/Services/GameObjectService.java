package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Repositories.GameObjectRepository;
import org.springframework.stereotype.Service;

@Service
public class GameObjectService {

    private final GameObjectRepository gameObjectRepository;

    public GameObjectService(GameObjectRepository gameObjectRepository) {
        this.gameObjectRepository = gameObjectRepository;
    }
}
