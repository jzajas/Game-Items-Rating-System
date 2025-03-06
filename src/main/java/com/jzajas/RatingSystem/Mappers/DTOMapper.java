package com.jzajas.RatingSystem.Mappers;

import com.jzajas.RatingSystem.DTOs.CommentDTO;
import com.jzajas.RatingSystem.DTOs.UserDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;

public class DTOMapper {

    public CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();

        dto.setMessage(comment.getMessage());
        dto.setAuthorFirstName(comment.getAuthorID().getFirstName());
        dto.setAuthorLastName(comment.getAuthorID().getLastName());
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
}
