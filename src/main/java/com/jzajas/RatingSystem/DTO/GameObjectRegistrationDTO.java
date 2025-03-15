package com.jzajas.RatingSystem.DTO;

import com.jzajas.RatingSystem.Entities.GameCategory;
import com.jzajas.RatingSystem.Entities.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameObjectRegistrationDTO {

    @NotNull(message = "Title required")
    @NotBlank(message = "Title required")
    private String title;

    private String text;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category required")
    private GameCategory category;
}
