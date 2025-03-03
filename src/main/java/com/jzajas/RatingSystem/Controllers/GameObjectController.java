package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Services.GameObjectService;
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

//    TODO only owner can edit object
    @PutMapping("/{objectId}")
    public ResponseEntity<?> editGameObject(@PathVariable Long objectId, @RequestBody GameObject gameObject) {
        try {
            GameObject newGameObject = gameObjectService.updateGameObject(objectId, gameObject);
            return ResponseEntity.ok(newGameObject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewGameObject(@RequestBody GameObject gameObject) {
        try {
            GameObject savedGameObject = gameObjectService.createNewGameObject(gameObject);
            return ResponseEntity.ok(savedGameObject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getGameObjects() {
        try {
            List<GameObject> allGameObjects = gameObjectService.getAllGameObjects();
            return ResponseEntity.ok(allGameObjects);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    TODO only owner can delete object
    @DeleteMapping("/{gameObjectId}")
    public ResponseEntity<?> deleteGameObject(@PathVariable Long gameObjectId) {
        try {
            gameObjectService.deleteGameObjectById(gameObjectId);
            return ResponseEntity.ok("Comment deleted successfully ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
