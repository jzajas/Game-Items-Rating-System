package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.UserDTO;
import com.jzajas.RatingSystem.DTO.UserLoginDTO;
import com.jzajas.RatingSystem.DTO.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.GameCategory;
import com.jzajas.RatingSystem.Services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserRegistrationDTO dto) {
        userService.createNewUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    TODO useless?
    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@Valid @RequestBody UserLoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(HttpStatus.OK);
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

    @GetMapping("/score")
    public ResponseEntity<List<UserScoreDTO>> getTopSellersForCategory(
            @RequestParam(defaultValue = "10") int display,
            @RequestParam(required = false) GameCategory category
    ) {
        List<UserScoreDTO> topSellers = userService.getTopSellers(display, category);
        return ResponseEntity.ok(topSellers);
    }
}
