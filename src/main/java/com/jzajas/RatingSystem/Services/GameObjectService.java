package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Repositories.GameObjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameObjectService {

    private final GameObjectRepository gameObjectRepository;

    public GameObjectService(GameObjectRepository gameObjectRepository) {
        this.gameObjectRepository = gameObjectRepository;
    }


    public GameObject createNewGameObject(GameObject newGameObject) {
        return gameObjectRepository.save(newGameObject);
    }

//    TODO only author can edit -> might want to modify an endpoint
//     or something from sessions (current logged user == author_id)
    @Transactional
    public GameObject updateGameObject(Long objectId, GameObject newGameObject) {
        GameObject oldGameObject = gameObjectRepository
                .findById(objectId)
                .orElseThrow(() -> new IllegalArgumentException("Object with provided id does not exist"));

//        Basic author authentication
        if (!oldGameObject.getAuthorID().getId().equals(newGameObject.getAuthorID().getId())) {
            throw new RuntimeException("Only user that created object can edit it");
        }
        oldGameObject.setTitle(newGameObject.getTitle());
        oldGameObject.setText(newGameObject.getText());

        return gameObjectRepository.save(oldGameObject);

    }

    public List<GameObject> getAllGameObjects() {
        return gameObjectRepository.findAll();
    }

    public void deleteGameObjectById(Long gameObjectId) {
        gameObjectRepository.deleteById(gameObjectId);
    }
}
