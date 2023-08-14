package com.example.water.domain.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class MypageResponseDTO {
    private int status;
    private String message;
    private MypageDTO data;

}