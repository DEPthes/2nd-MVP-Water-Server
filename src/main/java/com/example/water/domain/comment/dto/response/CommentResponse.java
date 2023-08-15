package com.example.water.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private Long userId;
    private Long myCrystalCount;
    private String content;
    private LocalDate date;
}