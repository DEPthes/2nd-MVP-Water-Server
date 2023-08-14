package com.example.water.domain.comment.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private Long userId;
    private Long myCrystalCount;
    private String content;
    private LocalDate date;
}