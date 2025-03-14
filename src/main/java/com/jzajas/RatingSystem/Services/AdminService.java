package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.UserRegistrationDTO;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final DTOMapper mapper;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public AdminService(CommentRepository commentRepository, DTOMapper mapper,
                        PasswordEncoder encoder, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.mapper = mapper;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createAdmin(UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) throw new EmailAlreadyInUseException(dto.getEmail());

        User user = mapper.convertFromUserRegistrationDTOtoAdmin(dto);
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllPendingUsers() {
        return userRepository.findAllUsersWithPendingStatus();
    }

    @Transactional
    public void approveUser(Long id) {
        User user = findUserByID(id);
        user.setStatus(Status.APPROVED);
        userRepository.save(user);
    }

    @Transactional
    public void declineUser(Long id) {
        User user = findUserByID(id);
        user.setStatus(Status.DECLINED);
        userRepository.save(user);
    }


    @Transactional(readOnly = true)
    private User findUserByID(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

}
