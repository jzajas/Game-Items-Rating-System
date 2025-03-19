package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTO.Input.CommentCreationDTO;
import com.jzajas.RatingSystem.DTO.Input.UserAndCommentCreationDTO;
import com.jzajas.RatingSystem.DTO.Output.CommentDTO;
import com.jzajas.RatingSystem.Services.Implementations.CommentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class CommentController {

    private final CommentServiceImpl commentServiceImpl;


    public CommentController(CommentServiceImpl commentServiceImpl) {
        this.commentServiceImpl = commentServiceImpl;
    }

    //    @PreAuthorize("hasRole('SELLER') OR hasRole('ADMINISTRATOR') OR isAnonymous()")
    @PostMapping("/{userId}/comments")
    public ResponseEntity<Void> addCommentForUser(
            @PathVariable Long userId,
            @Valid @RequestBody CommentCreationDTO dto,
            Authentication authentication
    ) {
        commentServiceImpl.createNewComment(dto, userId, authentication);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //    @PreAuthorize("isAnonymous()")
    @PostMapping("/comments/create")
    public ResponseEntity<Void> createCommentWithUser(@Valid @RequestBody UserAndCommentCreationDTO dto) {
        commentServiceImpl.createNewCommentWithUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> getSpecificComment(@PathVariable Long commentId) {
        CommentDTO comment = commentServiceImpl.findCommentDTOById(commentId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{userId}/comments/posted")
    public ResponseEntity<List<CommentDTO>> getAllSellersPostedComments(@PathVariable Long userId) {
        List<CommentDTO> allPostedComments = commentServiceImpl.findAllUserComments(userId, true);
        return ResponseEntity.ok(allPostedComments);
    }

    @GetMapping("/{userId}/comments/received")
    public ResponseEntity<List<CommentDTO>> getAllSellersReceivedComments(@PathVariable Long userId) {
        List<CommentDTO> allReceivedComments = commentServiceImpl.findAllUserComments(userId, false);
        return ResponseEntity.ok(allReceivedComments);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentCreationDTO dto,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        commentServiceImpl.updateCommentById(commentId, dto, userEmail);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        commentServiceImpl.deleteCommentById(commentId, userEmail);
        return ResponseEntity.noContent().build();
    }
}
