package com.jzajas.RatingSystem.Controllers;

import com.jzajas.RatingSystem.Services.CommentService;
import lombok.Builder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class CommentController {

    private final CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/users/{id}/comments")
    public void addCommentForUser(@PathVariable Long userId) {

    }

    @GetMapping("/users/{id}/comments")
    public void listSellersComments(@PathVariable Long userId) {

    }

    @GetMapping("/users/{id}/comments/{id}")
    public void viewSpecificComment(@PathVariable Long userId, @PathVariable Long commentID) {

    }

//    TODO only author can delete its own comment
    @DeleteMapping("/users/{id}/comments/{id}")
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentID) {

    }

    @PutMapping("/users/{id}/comments")
    public void updateComment(@PathVariable Long userId) {

    }
}
