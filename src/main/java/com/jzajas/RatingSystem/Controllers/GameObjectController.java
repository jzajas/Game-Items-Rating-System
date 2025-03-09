package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.GameObjectDTO;
import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Services.GameObjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/object")
public class GameObjectController {

    private final GameObjectService gameObjectService;


    public GameObjectController(GameObjectService gameObjectService) {
        this.gameObjectService = gameObjectService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> addNewGameObject(@RequestBody GameObject gameObject) {
        gameObjectService.createNewGameObject(gameObject);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<GameObjectDTO>> getGameObjects() {
        List<GameObjectDTO> allGameObjects = gameObjectService.getAllGameObjects();
        return ResponseEntity.ok(allGameObjects);
    }

//    TODO only owner can edit object
    @PutMapping("/{objectId}")
    public ResponseEntity<Void> editGameObject(@PathVariable Long objectId, @RequestBody GameObject gameObject) {
        gameObjectService.updateGameObject(objectId, gameObject);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    TODO only owner can delete object
    @DeleteMapping("/{gameObjectId}")
    public ResponseEntity<Void> deleteGameObject(@PathVariable Long gameObjectId) {
        gameObjectService.deleteGameObjectById(gameObjectId);
        return ResponseEntity.noContent().build();
    }
}
