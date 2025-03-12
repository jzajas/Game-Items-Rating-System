package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.UserDTO;
import com.jzajas.RatingSystem.DTO.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.GameCategory;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final DTOMapper mapper;

    public UserService(UserRepository userRepository, DTOMapper mapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Transactional
    public void createNewUser(User user) {

        log.info(user.getEmail());
        log.info(user.getEmail());
        log.info(user.getEmail());

        if (emailAlreadyExists(user.getEmail())) {
           throw new EmailAlreadyInUseException();
        }

        user.setPassword(encoder.encode(user.getPassword()));
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
            throw new UserNotFoundException(id);
        }
    }

    @Transactional(readOnly = true)
    public List<UserScoreDTO> getTopSellers(int number) {
        return userRepository.findTopSellersByRating(number);
    }

    @Transactional(readOnly = true)
    public List<UserScoreDTO> getTopSellersByCategory(int number, GameCategory category) {
        return userRepository.findTopSellersByRatingAndCategory(number, String.valueOf(category));
    }

    @Transactional(readOnly = true)
    public double calculateUserScore(Long id) {
        List<Comment> commentList = getAllCommentsForUserById(id);

        return commentList
                .parallelStream()
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0);
    }

    @Transactional(readOnly = true)
    private boolean emailAlreadyExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}

