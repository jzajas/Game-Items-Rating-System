package com.jzajas.RatingSystem.Entities;

import com.jzajas.RatingSystem.DTO.UserScoreDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@NamedNativeQuery(
        name = "find_top_sellers",
        query = "SELECT u.first_name, u.last_name, u.created_at, " +
                "COALESCE(AVG(c.rating), 0) as avg_score, " +
                "COUNT(c.id) as comment_count " +
                "FROM users u " +
                "LEFT JOIN comments c ON u.id = c.receiver " +
//                "WHERE u.status = 'Approved' " +
                "GROUP BY u.id " +
                "ORDER BY avg_score DESC " +
                "LIMIT :limit",
        resultSetMapping = "user_score_dto"
)
@NamedNativeQuery(
        name = "find_top_sellers_by_category",
        query = "SELECT u.first_name, u.last_name, u.created_at, " +
                "COALESCE(AVG(c.rating), 0) as avg_score, " +
                "COUNT(c.id) as comment_count " +
                "FROM users u " +
                "INNER JOIN game_objects g ON u.id = g.author_id " +
                "LEFT JOIN comments c ON u.id = c.receiver " +
                "WHERE g.category = :category " +
//                "WHERE g.category = :category AND u.status = Approved " +
                "GROUP BY u.id " +
                "ORDER BY avg_score DESC " +
                "LIMIT :limit",
        resultSetMapping = "user_score_dto"
)
@SqlResultSetMapping(
        name = "user_score_dto",
        classes = @ConstructorResult(
                targetClass = UserScoreDTO.class,
                columns = {
                        @ColumnResult(name = "first_name", type = String.class),
                        @ColumnResult(name = "last_name", type = String.class),
                        @ColumnResult(name = "created_at", type = Date.class),
                        @ColumnResult(name = "avg_score", type = Double.class),
                        @ColumnResult(name = "comment_count", type = Integer.class),
                }
        )
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "status", nullable = false)
    private Status status;


    @PrePersist
    public void initializeNewUser() {
        this.createdAt = new Date();
        if (this.role == null) this.role = Role.SELLER;
        if (this.status == null) this.status = Status.PENDING;
    }
}
