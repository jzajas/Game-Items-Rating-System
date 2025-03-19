package com.jzajas.RatingSystem.Services.Interfaces;

import com.jzajas.RatingSystem.DTO.Input.CommentCreationDTO;
import com.jzajas.RatingSystem.DTO.Input.UserAndCommentCreationDTO;
import com.jzajas.RatingSystem.DTO.Output.CommentDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {

    @Transactional
    void createNewComment(CommentCreationDTO dto, Long receiverId, Authentication authentication);

    @Transactional
    void createNewCommentWithUser(UserAndCommentCreationDTO dto);

    @Transactional(readOnly = true)
    CommentDTO findCommentDTOById(Long id);

    @Transactional(readOnly = true)
    Comment findCommentById(Long id);

    @Transactional(readOnly = true)
    List<CommentDTO> findAllUserComments(Long id, boolean Posted);

    @Transactional
    void updateCommentById(Long commentId, CommentCreationDTO dto, String email);

    @Transactional
    void deleteCommentById(Long commentId, String email);
}
