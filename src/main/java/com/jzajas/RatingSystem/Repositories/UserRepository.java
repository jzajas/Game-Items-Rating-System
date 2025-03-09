package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.DTO.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = "SELECT * FROM comments WHERE receiver = :userId", nativeQuery = true)
    List<Comment> findAllCommentsForUserById(@Param("userId") Long userId);

    @Query(name = "find_top_sellers", nativeQuery = true)
    List<UserScoreDTO> findTopSellersByRating(@Param("limit") int limit);

    @Query(name = "find_top_sellers_by_category", nativeQuery = true)
    List<UserScoreDTO> findTopSellersByRatingAndCategory(@Param("limit") int limit, @Param("category") String category);
}
