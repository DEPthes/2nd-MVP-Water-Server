package com.example.water.domain.diary.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiaryResponse {
    private String diary;
    private LocalDateTime localDateTime;
    private Long emotionId;
    private Long myCrystalCount;

    @Builder
    public DiaryResponse(String diary, LocalDateTime localDateTime, Long emotionId, Long myCrystalCount) {
        this.diary = diary;
        this.localDateTime = localDateTime;
        this.emotionId = emotionId;
        this.myCrystalCount = myCrystalCount;
    }

    public static DiaryResponse of(String diary, LocalDateTime localDateTime, Long emotionId, Long myCrystalCount) {
        return DiaryResponse.builder()
                .diary(diary)
                .localDateTime(localDateTime)
                .emotionId(emotionId)
                .myCrystalCount(myCrystalCount)
                .build();
    }
}