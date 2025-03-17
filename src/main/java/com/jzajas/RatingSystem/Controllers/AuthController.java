package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.Input.ForgotPasswordRequestDTO;
import com.jzajas.RatingSystem.DTO.Input.PasswordResetDTO;
import com.jzajas.RatingSystem.Services.AuthService;
import com.jzajas.RatingSystem.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    private final AuthService authService;


    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto, Authentication authentication) {
        authService.sendResetCode(dto, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        authService.resetPassword(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check_code")
    public ResponseEntity<Boolean> checkCode(@RequestParam String code, Authentication authentication) {
        boolean valid = authService.checkCode(code, authentication);
        return ResponseEntity.ok(valid);
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestParam(name = "token") String token) {
        userService.confirmUserEmail(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
