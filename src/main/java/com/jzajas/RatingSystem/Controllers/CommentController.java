package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Services.CommentService;
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
    public ResponseEntity<?> addCommentForUser(@PathVariable Long userId, @RequestBody Comment comment) {
        try {
            Comment savedComment = commentService.createNewComment(comment, userId);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<?> listSellersComments(@PathVariable Long userId) {
        try {
            List<Comment> allUserComments = commentService.findAllUserComments(userId);
            return ResponseEntity.ok(allUserComments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<?> viewSpecificComment(@PathVariable Long userId, @PathVariable Long commentId) {
        try {
            Comment comment = commentService.findCommentById(commentId);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    TODO only author can delete its own comment --> sessions?
    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        try {
            commentService.deleteCommentById(commentId);
            return ResponseEntity.ok("Comment deleted successfully ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long userId, @PathVariable Long commentId, @RequestBody Comment comment) {
        try {
            Comment newComment = commentService.updateCommentById(commentId, comment);
            return ResponseEntity.ok(newComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
