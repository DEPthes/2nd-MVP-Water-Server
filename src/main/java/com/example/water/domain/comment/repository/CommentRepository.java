package com.example.water.domain.comment.repository;

import com.example.water.domain.comment.entity.Comment;
import com.example.water.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByUserId(User user);
    Comment findFirstByUserIdOrderByDate(User user);

    List<Comment> findAllByMyCrystalCountAndUserId(Long myCrystalCount, User user);
}