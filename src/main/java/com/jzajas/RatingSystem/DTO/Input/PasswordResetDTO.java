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
public class PasswordResetDTO {

    @Email
    @NotNull(message = "email required")
    @NotBlank(message = "email required")
    private String email;

    @NotNull(message = "password required")
    @NotBlank(message = "password required")
    private String password;

    @NotNull(message = "code required")
    @NotBlank(message = "code required")
    private String code;
}
