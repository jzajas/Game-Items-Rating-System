package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    String TOP_USERS_QUERY =
            "SELECT users.id, users.first_name, users.last_name, users.created_at, " +
                    "COALESCE(AVG(comments.rating), 0) as avg_score " +
                    "FROM users " +
                    "LEFT JOIN comments ON users.id = comments.receiver " +
                    "GROUP BY users.id, users.first_name, users.last_name, users.created_at " +
                    "ORDER BY avg_score DESC " +
                    "LIMIT :limit";


    User findByEmail(String email);

    @Query(value = "SELECT * FROM comments WHERE receiver = :userId", nativeQuery = true)
    List<Comment> findAllCommentsForUserById(@Param("userId") Long userId);

    @Query(value = TOP_USERS_QUERY, nativeQuery = true)
    List<Object[]> findTopSellersByRating(@Param("limit") int limit);

}
