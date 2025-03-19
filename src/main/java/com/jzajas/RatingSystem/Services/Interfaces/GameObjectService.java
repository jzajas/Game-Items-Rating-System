package com.jzajas.RatingSystem.Services.Interfaces;

import com.jzajas.RatingSystem.DTO.Input.GameObjectCreationDTO;
import com.jzajas.RatingSystem.DTO.Output.GameObjectDTO;
import com.jzajas.RatingSystem.Entities.GameObject;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GameObjectService {

    @Transactional
    void createNewGameObject(GameObjectCreationDTO dto, Authentication authentication);

    @Transactional
    GameObject getGameObjectByID(Long id);

    @Transactional(readOnly = true)
    List<GameObjectDTO> getAllGameObjects();

    @Transactional
    void updateGameObject(Long objectId, GameObjectCreationDTO dto);

    @Transactional
    void deleteGameObjectById(Long gameObjectId);
}
