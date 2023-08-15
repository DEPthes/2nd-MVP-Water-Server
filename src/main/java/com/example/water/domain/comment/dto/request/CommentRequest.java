package com.example.water.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Getter
@NoArgsConstructor
//Front단에서 요청하는 DTO
public class CommentRequest implements Serializable {
    private String diary;
    private Long emotionId;
    private Long myCrystalCount;

    @Builder
    public CommentRequest(String diary, Long emotionId, Long myCrystalCount) {
        this.diary = diary;
        this.emotionId = emotionId;
        this.myCrystalCount = myCrystalCount;
    }

    public static CommentRequest of(String diary, Long emotionId, Long myCrystalCount) {
        return CommentRequest.builder()
                .diary(diary)
                .emotionId(emotionId)
                .myCrystalCount(myCrystalCount)
                .build();
    }
}