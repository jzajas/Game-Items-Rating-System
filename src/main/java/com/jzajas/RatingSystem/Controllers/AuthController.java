package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.Services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
