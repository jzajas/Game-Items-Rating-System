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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = true)
    private String message;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
//    @Column(name = "author_id", nullable = false)
    private User authorID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "rating", nullable = false)
    private int rating;


    @PrePersist
    public void addNewDateToComment() {
        this.createdAt = new Date();
    }
}
