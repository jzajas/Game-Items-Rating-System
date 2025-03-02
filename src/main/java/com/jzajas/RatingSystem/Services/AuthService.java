package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
