package com.jzajas.RatingSystem.Services.Implementations;

import com.jzajas.RatingSystem.AOP.LogExecutionTime;
import com.jzajas.RatingSystem.DTO.Input.ForgotPasswordRequestDTO;
import com.jzajas.RatingSystem.DTO.Input.PasswordResetDTO;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.AccountNotApprovedException;
import com.jzajas.RatingSystem.Exceptions.BadRequestException;
import com.jzajas.RatingSystem.Exceptions.UserEmailNotFoundException;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import com.jzajas.RatingSystem.Services.Interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final long RESET_PASSWORD_CODE_EXPIRATION_TIME = 10;
    private static final long ACCOUNT_CONFIRMATION_CODE_EXPIRATION_TIME = 60 * 24;

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final EmailServiceImpl emailServiceImpl;
    private final BCryptPasswordEncoder encoder;


    @Override
    @LogExecutionTime
    public String createAndSaveCreationToken(String email) {
        String code = generateCode();
        redisTemplate.opsForValue().set(code, email, Duration.ofMinutes(ACCOUNT_CONFIRMATION_CODE_EXPIRATION_TIME));
        return code;
    }

    @Override
    @LogExecutionTime
    public void sendResetCode(ForgotPasswordRequestDTO dto, Authentication authentication) {
        if (authentication != null && !Objects.equals(authentication.getName(), dto.getEmail())) {
            throw new BadRequestException("Logged in user email is different from the one in the request");
        }
        String email = dto.getEmail();
        userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        String resetCode = generateCode();
        redisTemplate.opsForValue().set(resetCode, email, Duration.ofMinutes(RESET_PASSWORD_CODE_EXPIRATION_TIME));
        emailServiceImpl.sendResetEmail(email, resetCode, RESET_PASSWORD_CODE_EXPIRATION_TIME);
    }

    @Override
    @LogExecutionTime
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

    @Override
    public String checkCodeValidity(String code, Authentication authentication) {
        if (!Objects.equals(authentication.getName(), redisTemplate.opsForValue().get(code))) {
            throw new BadRequestException("Provided key does not exist or it does not belong to you");
        }
        if (redisTemplate.hasKey(code)) return "Code is valid";
        return "Code is invalid";
    }

    @Override
    public void confirmUserEmail(String token) {
        String email = redisTemplate.opsForValue().get(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserEmailNotFoundException(email));
        user.setStatus(Status.PENDING_ADMIN);
        userRepository.save(user);
        redisTemplate.delete(email);
    }

    public void isAccountApproved(Status status) {
        if (status == Status.PENDING_EMAIL || status == Status.DECLINED) throw new AccountNotApprovedException();
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }
}
