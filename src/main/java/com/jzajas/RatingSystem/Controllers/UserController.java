package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTOs.UserDTO;
import com.jzajas.RatingSystem.DTOs.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        userService.createNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO userDTO = userService.findUserDTOById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{userId}/score")
    public ResponseEntity<Double> getUserScore(@PathVariable Long userId) {
        Double score = userService.calculateUserScore(userId);
        return ResponseEntity.ok(score);
    }
    
//    TODO @RequestParam might be got for specifying how many sellers to display
    @GetMapping("/score")
//    public void getTopSellers(RequestParam display) {
    public ResponseEntity<List<UserScoreDTO>> getTopSellers (@RequestParam(defaultValue = "10") int display) {
        List<UserScoreDTO> score = userService.getTopSellers(display);
        return ResponseEntity.ok(score);

    }
}
