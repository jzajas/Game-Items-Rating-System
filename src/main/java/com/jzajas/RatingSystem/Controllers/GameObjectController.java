package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.Services.GameObjectService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/object")
public class GameObjectController {

    private final GameObjectService gameObjectService;

    public GameObjectController(GameObjectService gameObjectService) {
        this.gameObjectService = gameObjectService;
    }

//    TODO only owner can edit object
    @PutMapping("/{id}")
    public void editObject(@PathVariable Long objectId) {

    }

    @PostMapping("/")
    public void addNewObject() {

    }

    @GetMapping("/")
    public void getObjects() {

    }

//    TODO only owner can delete object
    @DeleteMapping("/{id}")
    public void deleteObject(@PathVariable Long userId) {

    }
}
