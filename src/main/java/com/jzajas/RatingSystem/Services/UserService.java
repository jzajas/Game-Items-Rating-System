package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTOs.UserDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
           throw new EmailAlreadyInUseException();
        }
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public UserDTO findUserDTOById(Long id){
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapper.convertToUserDTO(user);
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllCommentsForUserById(Long id) {
        if (userRepository.existsById(id)) {
            return userRepository.findAllCommentsForUserById(id);
        } else {
            throw new UserNotFoundException("User with id: " + id + "could not be found");
        }
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    private boolean emailAlreadyExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}

