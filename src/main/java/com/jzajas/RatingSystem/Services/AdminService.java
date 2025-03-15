package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.PendingCommentDTO;
import com.jzajas.RatingSystem.DTO.Output.PendingUserDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.CommentNotFoundException;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<PendingUserDTO> getAllPendingUsers() {
        return userRepository
                .findAllUsersWithPendingStatus()
                .stream()
                .map(mapper::convertToPendingUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PendingCommentDTO> getAllPendingComments() {
        return commentRepository
                .findAllCommentsWithPendingStatus()
                .stream()
                .map(mapper::convertToPendingCommentDTO)
                .collect(Collectors.toList());
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

    @Transactional
    public void approveComment(Long id) {
        Comment comment = findCommentByID(id);
        comment.setStatus(Status.APPROVED);
        commentRepository.save(comment);
    }

    @Transactional
    public void declineComment(Long id) {
        Comment comment = findCommentByID(id);
        comment.setStatus(Status.DECLINED);
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    private User findUserByID(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    private Comment findCommentByID(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }
}
