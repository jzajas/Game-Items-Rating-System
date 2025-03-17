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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

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

    public void sendResetCode(ForgotPasswordRequestDTO dto, Authentication authentication) {
        if (authentication != null && !Objects.equals(authentication.getName(), dto.getEmail())) {
            throw new BadRequestException("Logged in user email is different from the one in the request");
        }
        String email = dto.getEmail();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        String resetCode = generateResetCode();
        redisTemplate.opsForValue().set(resetCode, email, Duration.ofMinutes(EXPIRATION_TIME));

        emailService.sendResetEmail(email, resetCode, EXPIRATION_TIME);
    }

    public void resetPassword(PasswordResetDTO dto) {
        String email = redisTemplate.opsForValue().get(dto.getCode());

        if (email == null) throw new BadRequestException("Code already expired or is incorrect");
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        user.setPassword(encoder.encode(dto.getPassword()));
        userRepository.save(user);
        redisTemplate.delete(dto.getCode());
    }

    public boolean checkCode(String code, Authentication authentication) {
        if (!Objects.equals(authentication.getName(), redisTemplate.opsForValue().get(code))) {
            throw new BadRequestException("Provided key does not exist or it does not belong to you");
        }
        return redisTemplate.hasKey(code);
    }

    public void isAccountApproved(Status status) {
          if (status == Status.PENDING_EMAIL|| status == Status.DECLINED) throw new AccountNotApprovedException();
    }

    private String generateResetCode() {
        return UUID.randomUUID().toString();
    }
}
