package com.example.water.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Mypage {
    private Long crystalCount;
    private Long sinceDate;
    private String nickname;
    private String image;

}