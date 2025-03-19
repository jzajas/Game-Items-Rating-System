package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.Input.GameObjectCreationDTO;
import com.jzajas.RatingSystem.DTO.Output.GameObjectDTO;
import com.jzajas.RatingSystem.Security.CustomSecurityExpressions;
import com.jzajas.RatingSystem.Services.Implementations.GameObjectServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/object")
public class GameObjectController {

    private final GameObjectServiceImpl gameObjectServiceImpl;


    public GameObjectController(GameObjectServiceImpl gameObjectServiceImpl) {
        this.gameObjectServiceImpl = gameObjectServiceImpl;
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/")
    public ResponseEntity<Void> addNewGameObject(@Valid @RequestBody GameObjectCreationDTO dto,
                                                 Authentication authentication
    ) {
        gameObjectServiceImpl.createNewGameObject(dto, authentication);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<GameObjectDTO>> getGameObjects() {
        List<GameObjectDTO> allGameObjects = gameObjectServiceImpl.getAllGameObjects();
        return ResponseEntity.ok(allGameObjects);
    }

    @PutMapping("/{objectId}")
    @PreAuthorize(CustomSecurityExpressions.GAME_OBJECT_OWNER_BY_ID_OR_ADMIN)
    public ResponseEntity<Void> editGameObject(@PathVariable Long objectId, @Valid @RequestBody GameObjectCreationDTO dto) {
        gameObjectServiceImpl.updateGameObject(objectId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{objectId}")
    @PreAuthorize(CustomSecurityExpressions.GAME_OBJECT_OWNER_BY_ID_OR_ADMIN)
    public ResponseEntity<Void> deleteGameObject(@PathVariable Long objectId) {
        gameObjectServiceImpl.deleteGameObjectById(objectId);
        return ResponseEntity.noContent().build();
    }
}
