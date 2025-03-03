package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;


//    TODO userService might be a optional dependency -> not in a constructor but in a setter
    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

//    TODO if the user is not found then there can be a prompt to log in/ set up account -> one of the scenarios
//    TODO posting user id, and receiving user id  should not be in the payload?
    @Transactional
    public Comment createNewComment(Comment comment, Long userId) {
        if (isRatingValid(comment.getRating())) {
            try {
                User user = userService.findUserById(userId);
                comment.setCommentReceiver(user);
                return commentRepository.save(comment);
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException(iae.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Illegal value of rating");
        }
    }

    public List<Comment> findAllUserComments(Long id) {
//        return commentRepository.findAllCommentsByUserId(id);
        return userService.getAllCommentsForUSerById(id);
    }

    public Comment findCommentById(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with that ID does not exist"));
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public Comment updateCommentById(Long id, Comment newComment) {
        Comment oldComment = commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with this id does not exist"));

        oldComment.setMessage(newComment.getMessage());

        if (isRatingValid(newComment.getRating())) {
            oldComment.setRating(newComment.getRating());
        } else {
            throw new IllegalArgumentException("Provided rating is not within bounds (1-10)");
        }
        return commentRepository.save(oldComment);
    }

    private boolean isRatingValid(int rating) {
        return rating >= 1 && rating <= 10;
    }
}
