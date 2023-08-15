package com.example.water.domain.comment.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class Color {
    private Long red;
    private Long green;
    private Long blue;

    @Builder
    public static Color of(Long red, Long green, Long blue) {
        return Color.builder()
                .red(red)
                .green(green)
                .blue(blue)
                .build();
    }
}