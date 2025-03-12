package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.CommentDTO;
import com.jzajas.RatingSystem.DTO.CommentRegistrationDTO;
import com.jzajas.RatingSystem.DTO.CommentUpdateDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.BadRequestException;
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
    @Transactional
    public void createNewComment(CommentRegistrationDTO dto, Long userId, boolean isAnonymous) {
        if (!isRatingValid(dto.getRating())) throw new InvalidRatingValueException(dto.getRating());
        if (userRepository.findById(userId).isEmpty()) throw new UserNotFoundException(userId);

        User user = userRepository.findById(userId).get();
        Comment comment;

        if (!isAnonymous) {
            comment = mapper.convertFromCommentRegistrationDTONotAnonymous(dto);
        } else {
            comment = mapper.convertFromCommentRegistrationDTOAnonymous(dto);
        }
        comment.setReceiver(user);
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
    public void updateCommentById(Long id, CommentUpdateDTO dto) {
        Comment oldComment = findCommentById(id);
        if (oldComment.getAuthorID() == null) throw new BadRequestException("Cannot update anonymous comment");

        if (isRatingValid(dto.getRating())) {
            oldComment.setRating(dto.getRating());
            oldComment.setMessage(dto.getMessage());
        } else {
            throw new InvalidRatingValueException(dto.getRating());
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
