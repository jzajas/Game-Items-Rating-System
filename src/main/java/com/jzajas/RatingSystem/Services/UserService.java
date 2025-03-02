package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createNewUser(User user) {
        if (emailAlreadyExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        return userRepository.save(user);
    }

    public User findUserByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalArgumentException("User with that id does not exist");
        }
    }

    private boolean emailAlreadyExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}

