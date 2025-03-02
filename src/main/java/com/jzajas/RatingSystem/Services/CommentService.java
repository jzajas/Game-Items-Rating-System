package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
            User user = userService.findUSerByID(userId);
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

    private boolean isRatingValid(int rating) {
        return rating > 0 && rating <= 10;
    }
}
