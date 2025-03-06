package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTOs.CommentDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;


    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;

    }

//    TODO if the user is not found then there can be a prompt to log in/ set up account -> one of the scenarios
//    TODO posting user id, and receiving user id  should not be in the payload?
    @Transactional
    public void createNewComment(Comment comment, Long userId) {
        if (isRatingValid(comment.getRating())) {
            try {
                User user = userService.findUserById(userId);
                comment.setReceiver(user);
                commentRepository.save(comment);
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException(iae.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Illegal value of rating");
        }
    }

//    TODO DTO
    public CommentDTO findCommentById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with that ID does not exist"));
        return convertToDTO(comment);
    }

//    TODO DTO
    public List<CommentDTO> findAllUserComments(Long id) {
        userService.findUserById(id);

        List<Comment> allComments =  commentRepository.findAllCommentsByUserId(id);
        return allComments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCommentById(Long id, Comment newComment) {
        Comment oldComment = commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with this id does not exist"));

        oldComment.setMessage(newComment.getMessage());

        if (isRatingValid(newComment.getRating())) {
            oldComment.setRating(newComment.getRating());
        } else {
            throw new IllegalArgumentException("Provided rating is not within bounds (1-10)");
        }
        commentRepository.save(oldComment);
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setMessage(comment.getMessage());
        dto.setAuthorFirstName(comment.getAuthorID().getFirstName());
        dto.setAuthorLastName(comment.getAuthorID().getLastName());
        dto.setReceiverFirstName(comment.getReceiver().getFirstName());
        dto.setReceiverLastName(comment.getReceiver().getLastName());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setRating(comment.getRating());

        return dto;
    }

    private boolean isRatingValid(int rating) {
        return rating >= 1 && rating <= 10;
    }
}
