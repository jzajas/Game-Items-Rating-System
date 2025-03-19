package com.jzajas.RatingSystem.Services.Interfaces;

import com.jzajas.RatingSystem.DTO.Input.ForgotPasswordRequestDTO;
import com.jzajas.RatingSystem.DTO.Input.PasswordResetDTO;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    @Transactional
    String createAndSaveCreationToken(String email);

    @Transactional
    void sendResetCode(ForgotPasswordRequestDTO dto, Authentication authentication);

    @Transactional
    void resetPassword(PasswordResetDTO dto);

    @Transactional(readOnly = true)
    String checkCodeValidity(String code, Authentication authentication);

    @Transactional
    void confirmUserEmail(String token);
}
