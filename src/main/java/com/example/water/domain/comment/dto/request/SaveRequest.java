package com.example.water.domain.comment.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class SaveRequest {
    private String comment;
    private Long emotionId;
    private Long myCrystalCount;

    @Builder
    public SaveRequest(String comment, Long emotionId, Long myCrystalCount) {
        this.comment = comment;
        this.emotionId = emotionId;
        this.myCrystalCount = myCrystalCount;
    }

    public static SaveRequest of(String comment, Long emotionId, Long myCrystalCount) {
        return SaveRequest.builder()
                .comment(comment)
                .emotionId(emotionId)
                .myCrystalCount(myCrystalCount)
                .build();
    }
}
