package com.jzajas.RatingSystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


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

//    TODO author_id is nullable because anonymous user can comment on sellers profile
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = true)
//    @Column(name = "author_id", nullable = true)
    private User authorID;

    @ManyToOne
    @JoinColumn(name = "comment_receiver", nullable = false)
//    @Column(name = "comment_receiver", nullable = false)
    private User commentReceiver;

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
