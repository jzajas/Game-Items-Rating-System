package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTOs.CommentDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.CommentNotFoundException;
import com.jzajas.RatingSystem.Exceptions.InvalidRatingValueException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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
//    TODO posting user id, and receiving user id  should not be in the payload?
    @Transactional
    public void createNewComment(Comment comment, Long userId) {
//        long commentReceiverId = comment.getReceiver().getId();
//        if (!Objects.equals(commentReceiverId, userId)) {
//            throw new BadRequestException(
//                    "Comment receiver id (" + commentReceiverId + ") does not match the id (" + userId + ") from URL"
//            );
//        }
        if (!isRatingValid(comment.getRating())) {
            throw new InvalidRatingValueException(comment.getRating());
        }
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        User user = userRepository.findById(userId).get();
        comment.setReceiver(user);
        System.out.println(user);
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public CommentDTO findCommentById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        return mapper.convertToCommentDTO(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> findAllUserComments(Long id, boolean Posted) {
        if(userRepository.existsById(id)) {
            List<Comment> allComments;
            if (Posted) {
                allComments =  commentRepository.findAllPostedCommentsByUserId(id);
            } else {
                allComments =  commentRepository.findAllReceivedCommentsByUserId(id);
            }
            return allComments.stream()
                    .map(mapper::convertToCommentDTO)
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Transactional
    public void updateCommentById(Long id, Comment newComment) {
        Comment oldComment = commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        oldComment.setMessage(newComment.getMessage());

        if (isRatingValid(newComment.getRating())) {
            oldComment.setRating(newComment.getRating());
        } else {
            throw new InvalidRatingValueException(newComment.getRating());
        }
        commentRepository.save(oldComment);
    }

    @Transactional
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    private boolean isRatingValid(int rating) {
        return rating >= 1 && rating <= 10;
    }
}
