package com.jzajas.RatingSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameObjectDTO {

    private String title;
    private String text;
    private Date updatedAt;
}
