package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.Input.ForgotPasswordRequestDTO;
import com.jzajas.RatingSystem.DTO.Input.PasswordResetDTO;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.AccountNotApprovedException;
import com.jzajas.RatingSystem.Exceptions.BadRequestException;
import com.jzajas.RatingSystem.Exceptions.UserEmailNotFoundException;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class AuthService {

    private static final long EXPIRATION_TIME = 10;

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;


    public AuthService(EmailService emailService, UserRepository userRepository,
                       StringRedisTemplate redisTemplate, BCryptPasswordEncoder encoder) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.encoder = encoder;
    }

    public void sendResetCode(ForgotPasswordRequestDTO dto) {
        String email = dto.getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        String resetCode = generateResetCode();
        redisTemplate.opsForValue().set(resetCode, email, Duration.ofMinutes(EXPIRATION_TIME));

        String body = "Your password reset code is: " + resetCode +
                "\n\nThis code will expire in " + EXPIRATION_TIME + " minutes.";

        emailService.sendResetEmail(email, body);
    }

    public void resetPassword(PasswordResetDTO dto) {
        String email = redisTemplate.opsForValue().get(dto.getCode());

//        TODO new exception
        if (!email.equals(dto.getEmail())) throw new BadRequestException("Wubudubu");
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        user.setPassword(encoder.encode(dto.getPassword()));
        userRepository.save(user);
        redisTemplate.delete(dto.getCode());
    }

    public boolean checkCode(String code) {
        return redisTemplate.hasKey(code);
//        return Boolean.TRUE.equals(redisTemplate.hasKey(code));
    }

    public void isAccountApproved(Status status) {
          if (status == Status.PENDING_EMAIL|| status == Status.DECLINED) throw new AccountNotApprovedException();
    }

    private String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);    }
}
