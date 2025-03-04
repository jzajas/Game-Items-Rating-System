package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comments WHERE comment_receiver = :userId", nativeQuery = true)
    List<Comment> findAllCommentsByUserId(@Param("userId") Long userId);
}
