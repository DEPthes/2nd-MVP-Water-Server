package com.example.water.domain.comment;

import com.example.water.domain.comment.DTO.CommentResponseDTO;
import com.example.water.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
//
//    List<Comment> findByUserIdAndMyCrystalCount(User user, Long myCrystalCount);
}
