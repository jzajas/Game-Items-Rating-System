package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.GameObjectDTO;
import com.jzajas.RatingSystem.DTO.GameObjectRegistrationDTO;
import com.jzajas.RatingSystem.DTO.GameObjectUpdateDTO;
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
    public void createNewGameObject(GameObjectRegistrationDTO dto) {
        GameObject newGameObject = mapper.convertFromGameObjectRegistrationDTO(dto);
        gameObjectRepository.save(newGameObject);
    }

    @Transactional
    public GameObject getGameObjectByID(Long id) {
        return gameObjectRepository
                .findById(id)
                .orElseThrow(() -> new GameObjectNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<GameObjectDTO> getAllGameObjects() {
        List<GameObject> allObjects = gameObjectRepository.findAll();
        return allObjects
                .stream()
                .map(mapper::convertToGameObjectDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateGameObject(Long objectId, GameObjectUpdateDTO dto) {
        GameObject oldGameObject = getGameObjectByID(objectId);
        oldGameObject.setTitle(dto.getTitle());
        oldGameObject.setText(dto.getText());
        oldGameObject.setCategory(dto.getCategory());
        gameObjectRepository.save(oldGameObject);
    }

    @Transactional
    public void deleteGameObjectById(Long gameObjectId) {
        gameObjectRepository.deleteById(gameObjectId);
    }
}
