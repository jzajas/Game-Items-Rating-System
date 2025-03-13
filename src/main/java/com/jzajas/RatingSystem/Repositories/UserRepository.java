package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.DTO.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM users u WHERE u.email = :email AND u.role = 'ADMINISTRATOR')",
            nativeQuery = true)
    boolean isAdministrator(@Param("email") String email);

    @Query(value = "SELECT * FROM comments WHERE receiver = :userId", nativeQuery = true)
    List<Comment> findAllCommentsForUserById(@Param("userId") Long userId);

    @Query(name = "find_top_sellers", nativeQuery = true)
    List<UserScoreDTO> findTopSellersByRating(@Param("limit") int limit);

    @Query(name = "find_top_sellers_by_category", nativeQuery = true)
    List<UserScoreDTO> findTopSellersByRatingAndCategory(@Param("limit") int limit, @Param("category") String category);
}
