package com.jzajas.RatingSystem.Mappers;

import com.jzajas.RatingSystem.DTO.Input.CommentCreationDTO;
import com.jzajas.RatingSystem.DTO.Input.GameObjectCreationDTO;
import com.jzajas.RatingSystem.DTO.Input.UserAndCommentCreationDTO;
import com.jzajas.RatingSystem.DTO.Input.UserRegistrationDTO;
import com.jzajas.RatingSystem.DTO.Output.*;
import com.jzajas.RatingSystem.Entities.*;
import org.springframework.stereotype.Component;


@Component
public class DTOMapper {


    public UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();

        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    public PendingUserDTO convertToPendingUserDTO(User user) {
        PendingUserDTO dto = new PendingUserDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setRole(user.getRole());

        return dto;
    }

    public User convertFromUserRegistrationDTOtoUser(UserRegistrationDTO dto) {
        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());

        return user;
    }

    public User convertFromUserRegistrationDTOtoAdmin(UserRegistrationDTO dto) {
        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole(Role.ADMINISTRATOR);
        user.setStatus(Status.APPROVED);

        return user;
    }

    public User convertFromUserAndCommentCreationDTOtoUser(UserAndCommentCreationDTO dto) {
        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(Role.SELLER);
        user.setStatus(Status.PENDING_ADMIN);

        return user;
    }

    public CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();

        dto.setMessage(comment.getMessage());
        if (comment.getAuthor() == null) {
            dto.setAuthorFirstName("Anonymous");
            dto.setAuthorLastName("User");
        } else {
            dto.setAuthorFirstName(comment.getAuthor().getFirstName());
            dto.setAuthorLastName(comment.getAuthor().getLastName());
        }
        dto.setReceiverFirstName(comment.getReceiver().getFirstName());
        dto.setReceiverLastName(comment.getReceiver().getLastName());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setRating(comment.getRating());

        return dto;
    }

    public PendingCommentDTO convertToPendingCommentDTO(Comment comment) {
        PendingCommentDTO dto = new PendingCommentDTO();


        if (comment.getAuthor() != null) {
            dto.setAuthorFirstName(comment.getAuthor().getFirstName());
            dto.setAuthorLastName(comment.getAuthor().getLastName());
        } else {
            dto.setAuthorFirstName(comment.getAnonymousUserDetails().getFirstName());
            dto.setAuthorLastName(comment.getAnonymousUserDetails().getLastName());
        }

        dto.setMessage(comment.getMessage());
        dto.setId(comment.getId());
        dto.setReceiverFirstName(comment.getReceiver().getFirstName());
        dto.setReceiverLastName(comment.getReceiver().getLastName());
        dto.setReceiverEmail(comment.getReceiver().getEmail());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setRating(comment.getRating());

        return dto;
    }

    public Comment convertFromCommentCreationDTONotAnonymous(CommentCreationDTO dto) {
        Comment comment = new Comment();

        if (dto.getMessage() != null) {
            comment.setMessage(dto.getMessage());
        }
        comment.setRating(dto.getRating());

        return comment;
    }

    public Comment convertFromUserAndCommentCreationDTOtoComment(UserAndCommentCreationDTO dto) {
        Comment comment = new Comment();

        if (dto.getMessage() != null) {
            comment.setMessage(dto.getMessage());
        }
        comment.setRating(dto.getRating());
        comment.setStatus(Status.PENDING_ADMIN);
        comment.setAuthor(null);

        return comment;
    }

    public AnonymousUserDetails convertFromCommentCreationDTOtoAnonymousUser(CommentCreationDTO dto) {
        AnonymousUserDetails anonymousUser = new AnonymousUserDetails();

        anonymousUser.setFirstName(dto.getFirstName());
        anonymousUser.setLastName(dto.getLastName());

        return anonymousUser;
    }

    public GameObjectDTO convertToGameObjectDTO(GameObject gameObject) {
        GameObjectDTO dto = new GameObjectDTO();

        dto.setTitle(gameObject.getTitle());
        dto.setText(gameObject.getText());
        dto.setUpdatedAt(gameObject.getUpdatedAt());
        dto.setCategory(String.valueOf(gameObject.getCategory()));

        return dto;
    }

    public GameObject convertFromGameObjectRegistrationDTO(GameObjectCreationDTO dto) {
        GameObject newGameObject = new GameObject();

        newGameObject.setTitle(dto.getTitle());
        newGameObject.setText(dto.getText());
        newGameObject.setCategory(dto.getCategory());

        return newGameObject;
    }
}
