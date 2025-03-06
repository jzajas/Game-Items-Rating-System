package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTOs.CommentDTO;
import com.jzajas.RatingSystem.DTOs.UserDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
//    private final UserService userService;
    private final DTOMapper mapper;
    private final UserRepository repo;


    public CommentService(CommentRepository commentRepository, UserService userService, DTOMapper mapper, UserRepository repo) {
        this.commentRepository = commentRepository;
//        this.userService = userService;
        this.mapper = mapper;
        this.repo = repo;
    }

//    TODO if the user is not found then there can be a prompt to log in/ set up account -> one of the scenarios
//    TODO posting user id, and receiving user id  should not be in the payload?
    @Transactional
    public void createNewComment(Comment comment, Long userId) {
        if (isRatingValid(comment.getRating()) && repo.existsById(userId)) {
            User user = repo.findById(userId).get();
            comment.setReceiver(user);
            commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("Illegal value of rating");
        }
    }

    public CommentDTO findCommentById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with that ID does not exist"));
        return mapper.convertToCommentDTO(comment);
    }

    public List<CommentDTO> findAllUserComments(Long id) {
        if(repo.existsById(id)) {
            List<Comment> allComments =  commentRepository.findAllCommentsByUserId(id);
            return allComments.stream()
                    .map(mapper::convertToCommentDTO)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("User with that id does not exist");
        }
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

    private boolean isRatingValid(int rating) {
        return rating >= 1 && rating <= 10;
    }
}
