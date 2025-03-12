package com.jzajas.RatingSystem.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    @NotNull
    @NotBlank(message = "First Name required")
    private String firstName;

    @NotNull
    @NotBlank(message = "Last Name required")
    private String lastName;

    @NotNull
    @NotBlank(message = "Password required")
    private String password;

    @Email
    @NotNull
    @NotBlank(message = "Email required")
    private String email;

}
