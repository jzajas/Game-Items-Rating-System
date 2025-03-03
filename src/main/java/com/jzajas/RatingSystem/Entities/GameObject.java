package com.jzajas.RatingSystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


//TODO might want ot add a game field for easier sorting and querying


@Data
@Entity
@Table(name = "game_objects")
@NoArgsConstructor
@AllArgsConstructor
public class GameObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
//    @Column(name = "author_id", nullable = false)
    private User authorID;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;


    @PrePersist
    public void addDateToNewObject() {
        this.createdAt = new Date();
    }

    @PreUpdate
    public void updateDateOfObject() {
        this.updatedAt = new Date();
    }
}
