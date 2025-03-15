package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.GameObjectDTO;
import com.jzajas.RatingSystem.DTO.GameObjectRegistrationDTO;
import com.jzajas.RatingSystem.DTO.GameObjectUpdateDTO;
import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.AccountNotApprovedException;
import com.jzajas.RatingSystem.Exceptions.GameObjectNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.GameObjectRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameObjectService {

    private final GameObjectRepository gameObjectRepository;
    private final DTOMapper mapper;
    private final UserRepository userRepository;


    public GameObjectService(GameObjectRepository gameObjectRepository, DTOMapper mapper, UserRepository userRepository) {
        this.gameObjectRepository = gameObjectRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;

    }

    @Transactional
    public void createNewGameObject(GameObjectRegistrationDTO dto, Authentication authentication) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.get().getStatus() != Status.APPROVED) {
            throw new AccountNotApprovedException("Account is not approved");
        }
        GameObject newGameObject = mapper.convertFromGameObjectRegistrationDTO(dto);
        newGameObject.setAuthorID(user.get());
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
