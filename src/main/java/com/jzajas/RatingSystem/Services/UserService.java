package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.Output.UserDTO;
import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.*;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Exceptions.UserEmailNotFoundException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final PasswordEncoder encoder;
    private final DTOMapper mapper;

    public UserService(CommentRepository commentRepository, UserRepository userRepository, EmailService emailService,
                       TokenService tokenService, PasswordEncoder encoder, DTOMapper mapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Transactional
    public void createNewUser(UserRegistrationDTO dto) {
        if (emailAlreadyExists(dto.getEmail())) throw new EmailAlreadyInUseException(dto.getEmail());

        User user = mapper.convertFromUserRegistrationDTOtoUser(dto);
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
        String token = tokenService.createAndSaveToken(user.getEmail());
        emailService.sendVerificationEmail(user.getEmail(), token);
    }

    @Transactional(readOnly = true)
    public UserDTO findUserDTOById(Long id){
        User user = userRepository
                .findUserWithApprovedStatus(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapper.convertToUserDTO(user);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email){
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllCommentsForUserById(Long id) {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);
        return commentRepository.findAllReceivedCommentsByUserId(id);
    }

    @Transactional(readOnly = true)
    public List<UserScoreDTO> getTopSellers(int number, GameCategory category) {
       if (category == null) {
            return userRepository.findTopSellersByRating(number);
       } else {
           return userRepository.findTopSellersByRatingAndCategory(number, String.valueOf(category));
       }
    }

    @Transactional(readOnly = true)
    public double calculateUserScore(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty() || user.get().getStatus() != Status.APPROVED || user.get().getRole() != Role.SELLER) {
            throw new UserNotFoundException(id);
        }
        List<Comment> commentList = getAllCommentsForUserById(id);

        return commentList
                .stream()
                .mapToDouble(Comment::getRating)
                .average()
                .orElse(0);
    }

    @Transactional(readOnly = true)
    private boolean emailAlreadyExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void confirmUserEmail(String token) {
        String email = tokenService.getEmailByToken(token);
        User user = findUserByEmail(email);
        user.setStatus(Status.PENDING_ADMIN);
        userRepository.save(user);
        tokenService.deleteToken(token);

    }
}

