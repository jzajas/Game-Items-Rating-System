package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.DTOs.CommentDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class CommentController {

    private final CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{userId}/comments")
    public ResponseEntity<Void> addCommentForUser(@PathVariable Long userId, @RequestBody Comment comment) {
        commentService.createNewComment(comment, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/comments/posted")
    public ResponseEntity<List<CommentDTO>> listSellersPostedComments(@PathVariable Long userId) {
        List<CommentDTO> allPostedComments = commentService.findAllUserComments(userId, true);
        return ResponseEntity.ok(allPostedComments);
    }

    @GetMapping("/{userId}/comments/received")
    public ResponseEntity<List<CommentDTO>> listSellersReceivedComments(@PathVariable Long userId) {
        List<CommentDTO> allReceivedComments = commentService.findAllUserComments(userId, false);
        return ResponseEntity.ok(allReceivedComments);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> viewSpecificComment(@PathVariable Long userId, @PathVariable Long commentId) {
        CommentDTO comment = commentService.findCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long userId, @PathVariable Long commentId, @RequestBody Comment comment) {
        commentService.updateCommentById(commentId, comment);
        return new ResponseEntity<>(HttpStatus.OK);

    }

//    TODO only author can delete its own comment --> sessions?
    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteCommentById(commentId);
        return ResponseEntity.noContent().build();
    }
}
