package com.example.water.domain.diary.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiaryRequest {
    private String diary;
    private LocalDateTime localDateTime;

    @Builder
    public DiaryRequest(String diary, LocalDateTime localDateTime) {
        this.diary = diary;
        this.localDateTime = localDateTime;
    }

    public static DiaryRequest of(String diary, LocalDateTime localDateTime) {
        return DiaryRequest.builder()
                .diary(diary)
                .localDateTime(localDateTime)
                .build();
    }
}
