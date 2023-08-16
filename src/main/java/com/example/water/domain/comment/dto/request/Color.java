package com.example.water.domain.comment.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
public class Color {
    private Long red;
    private Long green;
    private Long blue;

    public static Color of(Long red, Long green, Long blue) {
        return new Color(red, green, blue);
    }
}