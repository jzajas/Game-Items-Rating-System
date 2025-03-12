package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.CommentDTO;
import com.jzajas.RatingSystem.DTO.CommentRegistrationDTO;
import com.jzajas.RatingSystem.DTO.CommentUpdateDTO;
import com.jzajas.RatingSystem.Security.CustomSecurityExpressions;
import com.jzajas.RatingSystem.Services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class CommentController {

    private final CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('SELLER') OR isAnonymous")
    @PostMapping("/{userId}/comments")
    public ResponseEntity<Void> addCommentForUser(
            @PathVariable Long userId,
            @Valid @RequestBody CommentRegistrationDTO dto,
            Authentication authentication
    ) {
        commentService.createNewComment(dto, userId, authentication == null);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/comments/posted")
    public ResponseEntity<List<CommentDTO>> getAllSellersPostedComments(@PathVariable Long userId) {
        List<CommentDTO> allPostedComments = commentService.findAllUserComments(userId, true);
        return ResponseEntity.ok(allPostedComments);
    }

    @GetMapping("/{userId}/comments/received")
    public ResponseEntity<List<CommentDTO>> getAllSellersReceivedComments(@PathVariable Long userId) {
        List<CommentDTO> allReceivedComments = commentService.findAllUserComments(userId, false);
        return ResponseEntity.ok(allReceivedComments);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> getSpecificComment(@PathVariable Long commentId) {
        CommentDTO comment = commentService.findCommentDTOById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PostAuthorize(CustomSecurityExpressions.COMMENT_OWNER_BY_ID_OR_ADMIN)
    @PutMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long userId, @PathVariable Long commentId, @Valid @RequestBody CommentUpdateDTO dto) {
        commentService.updateCommentById(commentId, dto);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostAuthorize(CustomSecurityExpressions.COMMENT_OWNER_BY_ID_OR_ADMIN)
    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteCommentById(commentId);
        return ResponseEntity.noContent().build();
    }
}
