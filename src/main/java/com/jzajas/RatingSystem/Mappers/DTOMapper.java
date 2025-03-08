package com.jzajas.RatingSystem.Mappers;

import com.jzajas.RatingSystem.DTOs.CommentDTO;
import com.jzajas.RatingSystem.DTOs.GameObjectDTO;
import com.jzajas.RatingSystem.DTOs.UserDTO;
import com.jzajas.RatingSystem.DTOs.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.GameObject;
import com.jzajas.RatingSystem.Entities.User;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();

        dto.setMessage(comment.getMessage());
        if (comment.getAuthorID() == null) {
            dto.setAuthorFirstName(null);
            dto.setAuthorLastName(null);
        } else {
            dto.setAuthorFirstName(comment.getAuthorID().getFirstName());
            dto.setAuthorLastName(comment.getAuthorID().getLastName());
        }
        dto.setReceiverFirstName(comment.getReceiver().getFirstName());
        dto.setReceiverLastName(comment.getReceiver().getLastName());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setRating(comment.getRating());

        return dto;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();

        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    public UserScoreDTO convertToUserScoreDTO(User user) {
        UserScoreDTO dto = new UserScoreDTO();

        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreatedAt(user.getCreatedAt());
//        TODO this has to change in some way as well
        dto.setScore(0.0);

        return dto;
    }

    public GameObjectDTO convertToGameObjectDTO(GameObject gameObject) {
        GameObjectDTO dto = new GameObjectDTO();

        dto.setTitle(gameObject.getTitle());
        dto.setText(gameObject.getText());
        dto.setUpdatedAt(gameObject.getUpdatedAt());

        return dto;
    }
}
