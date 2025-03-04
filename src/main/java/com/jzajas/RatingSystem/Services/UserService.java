package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO make custom exception for providing incorrect id for user (and/ or comment and game object);

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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

    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with that ID does not exist"));
    }

    public double calculateUserScore(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with provided ID does not exist"));

//        List<Comment> commentList = userRepository.findAllCommentsForUserById(user.getId());
        List<Comment> commentList = getAllCommentsForUSerById(user.getId());

        double rating = commentList.stream()
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0);

        return rating;
    }

    public List<Comment> getAllCommentsForUSerById(Long id) {
        return userRepository.findAllCommentsForUserById(id);
    }

    private boolean emailAlreadyExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}

