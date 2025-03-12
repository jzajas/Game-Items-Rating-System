package com.jzajas.RatingSystem.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {

    @NotNull(message = "Message required")
    @NotBlank(message = "Message required")
    private String message;

    @NotNull(message = "Rating required")
    private int rating;
}
