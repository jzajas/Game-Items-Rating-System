package com.jzajas.RatingSystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserScoreDTO {

    private String firstName;
    private String lastName;
    private Date createdAt;
    private double score;
}
