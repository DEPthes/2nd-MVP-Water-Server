package com.example.water.domain.comment.repository;

import com.example.water.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByUserId(Long userId);
}