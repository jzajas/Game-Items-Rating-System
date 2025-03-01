package com.jzajas.RatingSystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//TODO might want to add rating field or something like that

@Data
@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    @Column(name = "message", nullable = true)
    private String message;

    //    TODO foreign key definition
    @Column(name = "author_id", nullable = false)
    private User authorID;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}
