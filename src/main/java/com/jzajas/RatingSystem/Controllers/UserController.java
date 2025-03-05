package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/score/{userId}")
    public ResponseEntity<Double> getUserScore(@PathVariable Long userId) {
        Double score = userService.calculateUserScore(userId);
        return ResponseEntity.ok(score);
    }
    
//    TODO @RequestParam might be got for specifying how many sellers to display
    @GetMapping("/score")
    public void getTopSellers(RequestParam display) {
//    public ResponseEntity<Double> getTopSellers(RequestParam display) {
//        double score = userService.calculateUserScore(userId);
//        return ResponseEntity.ok(score);

    }
}
