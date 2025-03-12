package com.jzajas.RatingSystem.Mappers;

import com.jzajas.RatingSystem.DTO.*;
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

    public User convertFromUserRegistrationDTO(UserRegistrationDTO dto) {
        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());

        return user;
    }

    public GameObjectDTO convertToGameObjectDTO(GameObject gameObject) {
        GameObjectDTO dto = new GameObjectDTO();

        dto.setTitle(gameObject.getTitle());
        dto.setText(gameObject.getText());
        dto.setUpdatedAt(gameObject.getUpdatedAt());
        dto.setCategory(String.valueOf(gameObject.getCategory()));

        return dto;
    }

    public GameObject convertFromGameObjectUpdateDTO(GameObjectUpdateDTO dto, GameObject oldGameObject) {

        oldGameObject.setTitle(dto.getTitle());
        oldGameObject.setText(dto.getText());
        oldGameObject.setCategory(dto.getCategory());

        return oldGameObject;
    }

    public GameObject convertFromGameObjectRegistrationDTO(GameObjectRegistrationDTO dto) {
        GameObject newGameObject = new GameObject();

        newGameObject.setTitle(dto.getTitle());
        newGameObject.setText(dto.getText());
        newGameObject.setAuthorID(dto.getAuthorId());
        newGameObject.setCategory(dto.getCategory());

        return newGameObject;
    }
}
