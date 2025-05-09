package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comments WHERE author = :userId AND status = 'APPROVED'", nativeQuery = true)
    List<Comment> findAllPostedCommentsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM comments WHERE receiver = :userId AND status = 'APPROVED'", nativeQuery = true)
    List<Comment> findAllReceivedCommentsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM comments WHERE status = 'PENDING_ADMIN'", nativeQuery = true)
    List<Comment> findAllCommentsWithPendingStatus();

    @Modifying
    @Query(value = "DELETE FROM comments c WHERE c.author = :id OR c.receiver = :id", nativeQuery = true)
    void deleteAllUserComments(@Param("id") Long id);
}
