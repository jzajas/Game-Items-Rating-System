package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.Services.TokenService;
import com.jzajas.RatingSystem.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/forgot_password")
    public void forgotPassword() {

    }

    @PostMapping("/reset")
    public void resetPassword() {

    }

    @GetMapping("/check_code")
    public void checkCode() {

    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestParam(name = "token") String token) {
        userService.confirmUserEmail(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
