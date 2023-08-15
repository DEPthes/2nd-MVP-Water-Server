package com.example.water.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MypageResponse {
    private int status;
    private String message;
    private Mypage data;

}