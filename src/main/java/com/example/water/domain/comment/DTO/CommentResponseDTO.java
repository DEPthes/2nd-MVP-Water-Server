package com.example.water.domain.comment.DTO;

import com.example.water.domain.comment.Comment;
import com.example.water.domain.user.User;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CommentResponseDTO {
    private Long commentId;
    private Long userId;
    private Long myCrystalCount;
    private String content;
    private LocalDate date;

//    public CommentResponseDTO(Long commentId, Long userId, Long myCrystalCount, String content, LocalDate date) {
//        this.commentId = commentId;
//        this.userId = userId;
//        this.myCrystalCount = myCrystalCount;
//        this.content = content;
//        this.date = date;
//    }
//
//    public CommentResponseDTO(Comment comment) {
//        this(comment.getCommentId(),
//                comment.getUserId().getUserId(),
//                comment.getMyCrystalCount(),
//                comment.getContent(),
//                comment.getDate());
//    }

}