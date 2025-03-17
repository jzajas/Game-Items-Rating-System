package com.jzajas.RatingSystem.DTO.Output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingCommentDTO {

    private long id;
    private String message;
    private String authorFirstName;
    private String authorLastName;
    private String authorEmail;
    private String receiverFirstName;
    private String receiverLastName;
    private String receiverEmail;
    private Date createdAt;
    private int rating;

}
