package com.jzajas.RatingSystem.Services.Implementations;

import com.jzajas.RatingSystem.AOP.LogExecutionTime;
import com.jzajas.RatingSystem.DTO.Input.CommentCreationDTO;
import com.jzajas.RatingSystem.DTO.Input.UserAndCommentCreationDTO;
import com.jzajas.RatingSystem.DTO.Output.CommentDTO;
import com.jzajas.RatingSystem.Entities.*;
import com.jzajas.RatingSystem.Exceptions.*;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.AnonymousUserDetailsRepository;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import com.jzajas.RatingSystem.Services.Interfaces.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AnonymousUserDetailsRepository anonymousUserDetailsRepository;
    private final DTOMapper mapper;
    private final BCryptPasswordEncoder encoder;


    @Override
    @LogExecutionTime
    public void createNewComment(CommentCreationDTO dto, Long receiverId, Authentication authentication) {
        if (!isRatingValid(dto.getRating())) throw new InvalidRatingValueException(dto.getRating());
        Optional<User> receiver = userRepository.findById(receiverId);
        if (receiver.isEmpty() ||
                receiver.get().getStatus() != Status.APPROVED ||
                receiver.get().getRole() != Role.SELLER) {
            throw new InvalidReceiverException(receiverId);
        }

        Comment comment = mapper.convertFromCommentCreationDTONotAnonymous(dto);

        if (authentication != null) {
            String authorEmail = authentication.getName();
            if (Objects.equals(receiver.get().getEmail(), authorEmail)) {
                throw new InvalidReceiverException(receiverId);
            }
            Optional<User> author = userRepository.findByEmail(authorEmail);
            if (author.isEmpty()) throw new UserEmailNotFoundException(authorEmail);
            if (author.get().getStatus() != Status.APPROVED) {
                throw new AccountNotApprovedException("Account is not approved");
            }
            comment.setAuthor(author.get());
            comment.setStatus(Status.APPROVED);
            comment.setAnonymousUserDetails(null);

        } else {
            if (dto.getFirstName() == null || dto.getLastName() == null) {
                throw new BadRequestException("Anonymous User needs to provide first and last name");
            }
            AnonymousUserDetails saved = anonymousUserDetailsRepository.save(
                    mapper.convertFromCommentCreationDTOtoAnonymousUser(dto)
            );
            comment.setAuthor(null);
            comment.setAnonymousUserDetails(saved);
            comment.setStatus(Status.PENDING_ADMIN);
        }
        comment.setReceiver(receiver.get());
        commentRepository.save(comment);
    }

    @Override
    @LogExecutionTime
    public void createNewCommentWithUser(UserAndCommentCreationDTO dto) {
        if (!isRatingValid(dto.getRating())) throw new InvalidRatingValueException(dto.getRating());
        if (userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new EmailAlreadyInUseException(dto.getEmail());

//        TODO fix this user creation -> id in endpoint for receiver?
        User user = mapper.convertFromUserAndCommentCreationDTOtoUser(dto);
        user.setPassword(encoder.encode(dto.getPassword()));
        User savedUser = userRepository.save(user);

        Comment comment = mapper.convertFromUserAndCommentCreationDTOtoComment(dto);
        comment.setReceiver(savedUser);

        AnonymousUserDetails anonymousUser = new AnonymousUserDetails();
        anonymousUser.setFirstName(dto.getFirstName());
        anonymousUser.setLastName(dto.getLastName());
        AnonymousUserDetails savedAnonymousUser = anonymousUserDetailsRepository.save(anonymousUser);

        comment.setAnonymousUserDetails(savedAnonymousUser);
        commentRepository.save(comment);
    }

    @Override
    public CommentDTO findCommentDTOById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        if (comment.getStatus() != Status.APPROVED) throw new CommentNotFoundException(id);
        return mapper.convertToCommentDTO(comment);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    @Override
    @LogExecutionTime
    public List<CommentDTO> findAllUserComments(Long id, boolean Posted) {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);

        List<Comment> allComments;
        if (Posted) {
            allComments = commentRepository.findAllPostedCommentsByUserId(id);
        } else {
            allComments = commentRepository.findAllReceivedCommentsByUserId(id);
        }

        return allComments.stream()
                .map(mapper::convertToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR') or @CustomSecurityExpressions.isCommentOwnerOrAdmin(#commentId, authentication)")
    public void updateCommentById(Long commentId, CommentCreationDTO dto, String email) {
        Comment oldComment = findCommentById(commentId);
        if (oldComment.getAuthor() == null && !userRepository.isAdministrator(email)) {
            throw new BadRequestException("Cannot update anonymous comment");
        }
        if (!isRatingValid(dto.getRating())) throw new InvalidRatingValueException(dto.getRating());

        if (dto.getMessage() != null) {
            oldComment.setMessage(dto.getMessage());
        }
        oldComment.setRating(dto.getRating());

        commentRepository.save(oldComment);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR') or @CustomSecurityExpressions.isCommentOwnerOrAdmin(#commentId, authentication)")
    public void deleteCommentById(Long commentId, String email) {
        Comment comment = findCommentById(commentId);
        if (comment.getAuthor() == null && !userRepository.isAdministrator(email)) {
            throw new BadRequestException("Cannot delete anonymous comment");
        }
        commentRepository.deleteById(commentId);
    }

    private boolean isRatingValid(int rating) {
        return rating >= 1 && rating <= 10;
    }
}
