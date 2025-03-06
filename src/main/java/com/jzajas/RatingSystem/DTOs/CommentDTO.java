package com.jzajas.RatingSystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String message;
    private String authorFirstName;
    private String authorLastName;
    private String receiverFirstName;
    private String receiverLastName;
    private Date createdAt;
    private int rating;
}

