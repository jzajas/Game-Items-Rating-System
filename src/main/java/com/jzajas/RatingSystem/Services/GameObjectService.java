package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTOs.GameObjectDTO;
import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Exceptions.GameObjectNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.GameObjectRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameObjectService {

    private final GameObjectRepository gameObjectRepository;
    private final DTOMapper mapper;

    public GameObjectService(GameObjectRepository gameObjectRepository, DTOMapper mapper) {
        this.gameObjectRepository = gameObjectRepository;
        this.mapper = mapper;
    }


    @Transactional
    public void createNewGameObject(GameObject newGameObject) {
        gameObjectRepository.save(newGameObject);
    }

    @Transactional(readOnly = true)
    public List<GameObjectDTO> getAllGameObjects() {
        List<GameObject> allObjects = gameObjectRepository.findAll();
        return allObjects
                .stream()
                .map(mapper::convertToGameObjectDTO)
                .collect(Collectors.toList());
    }

//    TODO only author can edit -> might want to modify an endpoint
//     or something from sessions (current logged user == author_id)
    @Transactional
    public void updateGameObject(Long objectId, GameObject newGameObject) {
        GameObject oldGameObject = gameObjectRepository
                .findById(objectId)
                .orElseThrow(GameObjectNotFoundException::new);

//        TODO basic authentication -> will likely change
        if (!oldGameObject.getAuthorID().getId().equals(newGameObject.getAuthorID().getId())) {
            throw new RuntimeException("Only user that created object can edit it");
        }
        oldGameObject.setTitle(newGameObject.getTitle());
        oldGameObject.setText(newGameObject.getText());

        gameObjectRepository.save(oldGameObject);
    }

    @Transactional
    public void deleteGameObjectById(Long gameObjectId) {
        gameObjectRepository.deleteById(gameObjectId);
    }
}
