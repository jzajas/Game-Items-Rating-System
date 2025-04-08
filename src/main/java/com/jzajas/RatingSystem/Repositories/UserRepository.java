package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.DTO.Output.UserScoreDTO;
import com.jzajas.RatingSystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM users u WHERE u.email = :email AND u.role = 'ADMINISTRATOR')",
            nativeQuery = true)
    boolean isAdministrator(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.status = 'APPROVED' AND u.role = 'SELLER'")
    Optional<User> findUserWithApprovedStatus(@Param("id") Long id);

    @Query(value = "SELECT * FROM users WHERE status = 'PENDING_ADMIN'", nativeQuery = true)
    List<User> findAllUsersWithPendingStatus();

    @Query(name = "find_top_sellers", nativeQuery = true)
    List<UserScoreDTO> findTopSellersByRating(@Param("limit") int limit,
                                              @Param("from") double from,
                                              @Param("to") double to);

    @Query(name = "find_top_sellers_by_category", nativeQuery = true)
    List<UserScoreDTO> findTopSellersByRatingAndCategory(@Param("limit") int limit,
                                                         @Param("from") double from,
                                                         @Param("to") double to,
                                                         @Param("category") String category);
}
