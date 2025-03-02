package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;


    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Transactional
    public Comment createNewComment(Comment comment, Long userId) {
        try{
            User user = userService.findUserByID(userId);
            comment.setAuthorID(user);
            if (isRatingValid(comment.getRating())) {
                return commentRepository.save(comment);
            } else {
                throw new IllegalArgumentException("Illegal value of rating");
            }
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException(iae.getMessage());
        }
    }

    public List<Comment> findAllUserComments(Long id) {
        return commentRepository.findAllByUserId(id);
    }

    public Comment findCommentById(Long id) {
//        Optional<Comment> comment = commentRepository.findById(id);
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with that ID does not exist"));
//        if(comment.isPresent()) {
//            return comment.get();
//        } else {
//            throw new IllegalArgumentException("Comment with that ID does not exist");
//        }
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public Comment updateCommentById(Long id, Comment newComment) {

        //        Comment oldComment = commentRepository
//                .findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Comment with this id does not exist"));
//
//        oldComment.setMessage(newComment.getMessage());
//        if (isRatingValid(newComment.getRating())) {
//            oldComment.setRating(newComment.getRating());
//        } else {
//            throw new IllegalArgumentException("Provided rating is not within bounds (1-10)");
//        }
//        return commentRepository.save(oldComment);


        return commentRepository.findById(id)
                .map(comment -> {
                    comment.setRating(newComment.getRating());
                    comment.setMessage(newComment.getMessage());
                    return commentRepository.save(comment);
                })
                .orElseGet(() -> {
                    return commentRepository.save(newComment);
                });
    }

    private boolean isRatingValid(int rating) {
        return rating >= 1 && rating <= 10;
    }
}
