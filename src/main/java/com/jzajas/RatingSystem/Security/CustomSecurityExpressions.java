package com.jzajas.RatingSystem.Security;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import com.jzajas.RatingSystem.Services.Implementations.GameObjectServiceImpl;
import com.jzajas.RatingSystem.Services.Interfaces.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component("CustomSecurityExpressions")
@RequiredArgsConstructor
public class CustomSecurityExpressions {

    public static final String GAME_OBJECT_OWNER_BY_ID_OR_ADMIN =
            "@gameObjectService.getGameObjectByID(#objectId).getAuthorID().getEmail() == authentication.name or " +
                    "hasRole('ADMINISTRATOR')";

    private final CommentService commentService;
    private final GameObjectServiceImpl gameObjectService;
    private final UserRepository userRepository;


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
                gameObject.getAuthorID() != null &&
                gameObject.getAuthorID().getEmail().equals(authentication.getName());
    }
}
