package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.CommentDTO;
import com.jzajas.RatingSystem.DTO.CommentRegistrationDTO;
import com.jzajas.RatingSystem.DTO.CommentUpdateDTO;
import com.jzajas.RatingSystem.Security.CustomSecurityExpressions;
import com.jzajas.RatingSystem.Services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PreAuthorize("hasRole('SELLER') OR hasRole('ADMINISTRATOR') OR isAnonymous()")
    @PostMapping("/{userId}/comments")
    public ResponseEntity<Void> addCommentForUser(
            @PathVariable Long userId,
            @Valid @RequestBody CommentRegistrationDTO dto,
            Authentication authentication
    ) {
        commentService.createNewComment(dto, userId, authentication);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> getSpecificComment(@PathVariable Long commentId) {
        CommentDTO comment = commentService.findCommentDTOById(commentId);
        return ResponseEntity.ok(comment);
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

    @PreAuthorize(CustomSecurityExpressions.COMMENT_HAS_OWNER_BY_ID_OR_ADMIN)
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDTO dto,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        commentService.updateCommentById(commentId, dto, userEmail);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PreAuthorize(CustomSecurityExpressions.COMMENT_HAS_OWNER_BY_ID_OR_ADMIN)
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        commentService.deleteCommentById(commentId, userEmail);
        return ResponseEntity.noContent().build();
    }
}
