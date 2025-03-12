package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.UserDTO;
import com.jzajas.RatingSystem.DTO.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.GameCategory;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final DTOMapper mapper;

    public UserController(UserService userService, DTOMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRegistrationDTO dto) {
        User user = mapper.convertFromUserRegistrationDTO(dto);
        userService.createNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    TODO login endpoint

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
    
    @GetMapping("/score")
    public ResponseEntity<List<UserScoreDTO>> getTopSellers(@RequestParam(defaultValue = "10") int display) {
        List<UserScoreDTO> topSellers = userService.getTopSellers(display);
        return ResponseEntity.ok(topSellers);

    }

    @GetMapping("/score/{category}")
    public ResponseEntity<List<UserScoreDTO>> getTopSellersForCategory(
            @RequestParam(defaultValue = "10") int display,
            @PathVariable GameCategory category
    ) {
        List<UserScoreDTO> topSellers = userService.getTopSellersByCategory(display, category);
        return ResponseEntity.ok(topSellers);
    }
}
