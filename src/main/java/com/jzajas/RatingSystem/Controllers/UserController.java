package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.UserDTO;
import com.jzajas.RatingSystem.DTO.Output.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.GameCategory;
import com.jzajas.RatingSystem.Services.Implementations.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRegistrationDTO dto) {
        userServiceImpl.createNewUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO userDTO = userServiceImpl.findUserDTOById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{userId}/score")
    public ResponseEntity<Double> getUserScore(@PathVariable Long userId) {
        Double score = userServiceImpl.calculateUserScore(userId);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/score")
    public ResponseEntity<List<UserScoreDTO>> getTopSellersForCategory(
            @RequestParam(defaultValue = "10") int display,
            @RequestParam(required = false) GameCategory category,
            @RequestParam(defaultValue = "0") double from,
            @RequestParam(defaultValue = "10") double to
    ) {
        List<UserScoreDTO> topSellers = userServiceImpl.getTopSellers(display, category, from, to);
        return ResponseEntity.ok(topSellers);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(Authentication authentication) {
        userServiceImpl.deleteUser(authentication);
        return ResponseEntity.noContent().build();
    }
}
