package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTOs.UserDTO;
import com.jzajas.RatingSystem.DTOs.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder encoder;
    private final DTOMapper mapper;

    public UserService(UserRepository userRepository, DTOMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
//        this.encoder = encoder;
    }

    @Transactional
    public void createNewUser(User user) {
        if (emailAlreadyExists(user.getEmail())) {
           throw new EmailAlreadyInUseException();
        }
//        user.setPassword(encoder.encode(user.getPassword()));
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

//        if (commentList.isEmpty()) {
//            return 0.0;
//        }

        return commentList
                .parallelStream()
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0);
    }

    @Transactional(readOnly = true)
    public List<UserScoreDTO> getTopSellers(int number) {
        List<Object[]> results = userRepository.findTopSellersByRating(number);

        return results.stream()
                .map(r -> {
                    UserScoreDTO dto = new UserScoreDTO();
                    dto.setFirstName((String) r[1]);
                    dto.setLastName((String) r[2]);
                    dto.setCreatedAt((Date) r[3]);
                    dto.setScore(((Number) r[4]).doubleValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private boolean emailAlreadyExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}

