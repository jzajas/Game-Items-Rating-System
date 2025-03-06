package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTOs.UserDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO make custom exception for providing incorrect id for user, comment and game object;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DTOMapper mapper;

    public UserService(UserRepository userRepository, DTOMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public void createNewUser(User user) {
        if (emailAlreadyExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with that ID does not exist"));
    }

    public UserDTO findUserDTOById(Long id){
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with that ID does not exist"));
        return mapper.convertToUserDTO(user);
    }

    public List<Comment> getAllCommentsForUserById(Long id) {
        if (userRepository.existsById(id)) {
            return userRepository.findAllCommentsForUserById(id);
        } else {
            throw new IllegalArgumentException("User with that id does not exist");
        }
    }

    public double calculateUserScore(Long id) {
        List<Comment> commentList = getAllCommentsForUserById(id);

        if (commentList.isEmpty()) {
            return 0.0;
        }
        return commentList.stream()
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0);
    }

    private boolean emailAlreadyExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}

