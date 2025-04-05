package com.jzajas.RatingSystem.DTO.Input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAndCommentCreationDTO {

    private String message;

    @NotNull(message = "Rating required")
    private int rating;

    @NotNull(message = "First Name required")
    @NotBlank(message = "First Name required")
    private String firstName;

    @NotNull(message = "Last Name required")
    @NotBlank(message = "Last Name required")
    private String lastName;

    @NotNull(message = "Password required")
    @NotBlank(message = "Password required")
    private String password;

    @Email
    @NotNull(message = "Email required")
    @NotBlank(message = "Email required")
    private String email;
}
