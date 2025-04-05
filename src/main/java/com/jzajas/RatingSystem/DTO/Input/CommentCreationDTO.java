package com.jzajas.RatingSystem.DTO.Input;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreationDTO {

    private String message;

    @NotNull(message = "Rating required")
    private int rating;

    private String firstName;
    private String lastName;
}
