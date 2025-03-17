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
public class ForgotPasswordRequestDTO {

    @Email
    @NotNull(message = "email required")
    @NotBlank(message = "email required")
    private String email;
}
