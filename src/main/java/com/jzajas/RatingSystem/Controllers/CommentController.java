package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Services.CommentService;
import com.jzajas.RatingSystem.Services.UserService;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public void listSellersComments(@PathVariable Long userId) {

    }

    @GetMapping("/{userId}/comments/{commentId}")
    public void viewSpecificComment(@PathVariable Long userId, @PathVariable Long commentId) {

    }

//    TODO only author can delete its own comment
    @DeleteMapping("/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {

    }

    @PutMapping("/{userId}/comments")
    public void updateComment(@PathVariable Long userId) {

    }
}
