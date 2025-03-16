package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.Output.CommentDTO;
import com.jzajas.RatingSystem.DTO.Input.CommentCreationDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.Role;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.*;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final DTOMapper mapper;
    private final UserRepository userRepository;


    public CommentService(CommentRepository commentRepository, DTOMapper mapper, UserRepository repo) {
        this.commentRepository = commentRepository;
        this.mapper = mapper;
        this.userRepository = repo;
    }

//    TODO if the user is not found then there can be a prompt to log in/ set up account -> one of the scenarios
    @Transactional
    public void createNewComment(CommentCreationDTO dto, Long receiverId, Authentication authentication) {
        Optional<User> receiver = userRepository.findById(receiverId);
        if (receiver.isEmpty()) throw new UserNotFoundException(receiverId);
        if (receiver.get().getStatus() != Status.APPROVED ||
                receiver.get().getRole() != Role.SELLER
        ) {
            throw new InvalidReceiverException(receiverId);
        }

        Comment comment = mapper.convertFromCommentRegistrationDTONotAnonymous(dto);

        if (authentication != null) {
            if (Objects.equals(receiver.get().getEmail(), authentication.getName())) {
                throw new InvalidReceiverException(receiverId);
            }
            Optional<User> author = userRepository.findByEmail(authentication.getName());
            if (author.isEmpty()) throw new UserEmailNotFoundException(authentication.getName());
            if (author.get().getStatus() != Status.APPROVED) {
                throw new AccountNotApprovedException("Account is not approved");
            }

            comment.setAuthorID(author.get());
            comment.setStatus(Status.APPROVED);
        } else {
            comment.setAuthorID(null);
            comment.setStatus(Status.PENDING_ADMIN);
        }
        if (!isRatingValid(dto.getRating())) throw new InvalidRatingValueException(dto.getRating());

        comment.setReceiver(receiver.get());
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public CommentDTO findCommentDTOById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        return mapper.convertToCommentDTO(comment);
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> findAllUserComments(Long id, boolean Posted) {
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);

        List<Comment> allComments;
        if (Posted) {
            allComments =  commentRepository.findAllPostedCommentsByUserId(id);
        } else {
            allComments =  commentRepository.findAllReceivedCommentsByUserId(id);
        }

        return allComments.stream()
                .map(mapper::convertToCommentDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCommentById(Long commentId, CommentCreationDTO dto, String email) {
        Comment oldComment = findCommentById(commentId);
        if (oldComment.getAuthorID() == null && !userRepository.isAdministrator(email)) {
            throw new BadRequestException("Cannot update anonymous comment");
        }

        if (isRatingValid(dto.getRating())) {
            oldComment.setRating(dto.getRating());
            oldComment.setMessage(dto.getMessage());
        } else {
            throw new InvalidRatingValueException(dto.getRating());
        }
        commentRepository.save(oldComment);
    }

    @Transactional
    public void deleteCommentById(Long commentId, String email) {
        Comment comment = findCommentById(commentId);
        if (comment.getAuthorID() == null && !userRepository.isAdministrator(email)) {
            throw new BadRequestException("Cannot delete anonymous comment");
        }
        commentRepository.deleteById(commentId);
    }

    private boolean isRatingValid(int rating) {
        return rating >= 1 && rating <= 10;
    }
}
