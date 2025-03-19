package com.jzajas.RatingSystem.Services.Implementations;

import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.UserDTO;
import com.jzajas.RatingSystem.DTO.Output.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.*;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Exceptions.UserEmailNotFoundException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.GameObjectRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import com.jzajas.RatingSystem.Services.Interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EmailServiceImpl emailServiceImpl;
    private final PasswordEncoder encoder;
    private final DTOMapper mapper;
    private final AuthServiceImpl authServiceImpl;
    private final GameObjectRepository gameObjectRepository;


    @Override
    public void createNewUser(UserRegistrationDTO dto) {
        if (emailAlreadyExists(dto.getEmail())) throw new EmailAlreadyInUseException(dto.getEmail());

        User user = mapper.convertFromUserRegistrationDTOtoUser(dto);
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
        String token = authServiceImpl.createAndSaveCreationToken(user.getEmail());
        emailServiceImpl.sendVerificationEmail(user.getEmail(), token);
    }

    @Override
    public UserDTO findUserDTOById(Long id) {
        User user = userRepository
                .findUserWithApprovedStatus(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapper.convertToUserDTO(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));
    }

    @Override
    public List<Comment> getAllCommentsForUserById(Long id) {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);
        return commentRepository.findAllReceivedCommentsByUserId(id);
    }

    @Override
    public List<UserScoreDTO> getTopSellers(int display, GameCategory category, double from, double to) {
        if (category == null) {
            return userRepository.findTopSellersByRating(display, from, to);
        } else {
            return userRepository.findTopSellersByRatingAndCategory(display, from, to, String.valueOf(category));
        }
    }

    @Override
    public void deleteUser(Authentication authentication) {
        if (authentication == null) throw new AccessDeniedException("No authentication provided");
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();
        commentRepository.deleteAllUserComments(user.getId());
        gameObjectRepository.deleteAllUserGameObjects(user.getId());
        userRepository.deleteById(user.getId());
    }

    @Override
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
}

