package com.example.water.domain.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MypageDTO {
    private Long crystal_count;
    private Long since_date;
    private String nickname;
    private String image;

}