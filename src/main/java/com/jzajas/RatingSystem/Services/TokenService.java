package com.jzajas.RatingSystem.Services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class TokenService {

    private static final long EXPIRATION_TIME = 60 * 24;

    private final RedisTemplate<String, String> redisTemplate;

    public TokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String createAndSaveToken(String email) {
        String token = generateToken();
        redisTemplate.opsForValue().set(token, email, Duration.ofMinutes(EXPIRATION_TIME));
        return token;
    }

    public String getEmailByToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteToken(String email) {
        redisTemplate.delete(email);
    }

    private String generateToken() {
        UUID token = UUID.randomUUID();
        return token.toString();
    }
}
