package com.jzajas.RatingSystem.Services.Implementations;

import com.jzajas.RatingSystem.DTO.Input.GameObjectCreationDTO;
import com.jzajas.RatingSystem.DTO.Output.GameObjectDTO;
import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.AccountNotApprovedException;
import com.jzajas.RatingSystem.Exceptions.GameObjectNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.GameObjectRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import com.jzajas.RatingSystem.Services.Interfaces.GameObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameObjectServiceImpl implements GameObjectService {

    private final GameObjectRepository gameObjectRepository;
    private final DTOMapper mapper;
    private final UserRepository userRepository;


    @Override
    public void createNewGameObject(GameObjectCreationDTO dto, Authentication authentication) {
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        if (user.get().getStatus() != Status.APPROVED) {
            throw new AccountNotApprovedException("Account is not approved");
        }
        GameObject newGameObject = mapper.convertFromGameObjectRegistrationDTO(dto);
        newGameObject.setAuthorID(user.get());
        gameObjectRepository.save(newGameObject);
    }

    @Override
    public GameObject getGameObjectByID(Long id) {
        return gameObjectRepository
                .findById(id)
                .orElseThrow(() -> new GameObjectNotFoundException(id));
    }

    @Override
    public List<GameObjectDTO> getAllGameObjects() {
        List<GameObject> allObjects = gameObjectRepository.findAll();
        return allObjects
                .stream()
                .map(mapper::convertToGameObjectDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateGameObject(Long objectId, GameObjectCreationDTO dto) {
        GameObject oldGameObject = getGameObjectByID(objectId);
        oldGameObject.setTitle(dto.getTitle());
        oldGameObject.setText(dto.getText());
        oldGameObject.setCategory(dto.getCategory());
        gameObjectRepository.save(oldGameObject);
    }

    @Override
    public void deleteGameObjectById(Long gameObjectId) {
        gameObjectRepository.deleteById(gameObjectId);
    }
}
