package com.jzajas.RatingSystem.Security;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Services.Implementations.GameObjectServiceImpl;
import com.jzajas.RatingSystem.Services.Interfaces.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component("CustomSecurityExpressions")
@RequiredArgsConstructor
public class CustomSecurityExpressions {

    private final CommentService commentService;
    private final GameObjectServiceImpl gameObjectService;

    public boolean isCommentOwnerOrAdmin(Long commentId, Authentication authentication) {
        if (authentication == null) return false;

        Comment comment = commentService.findCommentById(commentId);
        return comment != null &&
                comment.getAuthor() != null &&
                comment.getAuthor().getEmail().equals(authentication.getName());
    }

    public boolean isGameObjectOwnerOrAdmin(Long objectId, Authentication authentication) {
        if (authentication == null) return false;

        GameObject gameObject = gameObjectService.getGameObjectByID(objectId);
        return gameObject != null &&
                gameObject.getAuthor() != null &&
                gameObject.getAuthor().getEmail().equals(authentication.getName());
    }
}
