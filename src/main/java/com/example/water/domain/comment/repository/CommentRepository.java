package com.example.water.domain.comment.repository;

import com.example.water.domain.comment.Comment;
import com.example.water.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByUserId(User user);

    List<Comment> findAllByMyCrystalCount(Long myCrystalCount);
}