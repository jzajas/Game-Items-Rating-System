package com.jzajas.RatingSystem.Services.Interfaces;

import org.springframework.transaction.annotation.Transactional;

public interface EmailService {

    @Transactional
    void sendVerificationEmail(String receiverEmail, String verificationCode);

    @Transactional
    void sendResetEmail(String receiverEmail, String code, Long expirationTime);
}
