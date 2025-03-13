package com.jzajas.RatingSystem.Security;

import org.springframework.stereotype.Component;

@Component("CustomSecurityExpressions")
public class CustomSecurityExpressions {

    public static final String GAME_OBJECT_OWNER_BY_ID_OR_ADMIN =
            "@gameObjectService.getGameObjectByID(#objectId).getAuthorID().getEmail() == authentication.name or " +
                    "hasRole('ADMINISTRATOR')";

    public static final String COMMENT_OWNER_BY_ID_OR_ADMIN =
            "@commentService.findCommentById(#commentId).getAuthorID().getEmail() == authentication.name or " +
                    "hasRole('ADMINISTRATOR')";


    public static final String COMMENT_HAS_OWNER_BY_ID_OR_ADMIN =
            "(@commentService.findCommentById(#commentId).getAuthorID() != null and " +
                    "@commentService.findCommentById(#commentId).getAuthorID().getEmail() == authentication.name) or " +
                    "hasRole('ADMINISTRATOR')";

}
