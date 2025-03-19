package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.Input.ForgotPasswordRequestDTO;
import com.jzajas.RatingSystem.DTO.Input.PasswordResetDTO;
import com.jzajas.RatingSystem.Services.Implementations.AuthServiceImpl;
import com.jzajas.RatingSystem.Services.Implementations.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserServiceImpl userServiceImpl;

    private final AuthServiceImpl authServiceImpl;


    public AuthController(AuthServiceImpl authServiceImpl, UserServiceImpl userServiceImpl) {
        this.authServiceImpl = authServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto, Authentication authentication) {
        authServiceImpl.sendResetCode(dto, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetDTO dto) {
        authServiceImpl.resetPassword(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check_code")
    public ResponseEntity<String> checkCode(@RequestParam(name = "code") String code, Authentication authentication) {
        String response = authServiceImpl.checkCodeValidity(code, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestParam(name = "token") String token) {
        authServiceImpl.confirmUserEmail(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
