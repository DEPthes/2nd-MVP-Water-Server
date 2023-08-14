package com.example.water.domain.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MypageDTO {
    private Long crystalCount;
    private Long sinceDate;
    private String nickname;
    private String image;

}