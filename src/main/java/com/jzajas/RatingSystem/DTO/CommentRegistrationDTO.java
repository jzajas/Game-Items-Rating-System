package com.jzajas.RatingSystem.DTO;

import com.jzajas.RatingSystem.Entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRegistrationDTO {

    @NotNull(message = "Message required")
    @NotBlank(message = "Message required")
    private String message;

    private User authorID;

    @NotNull(message = "Receiver id required")
    private User receiverID;

    @NotNull(message = "Rating required")
    private int rating;
}
