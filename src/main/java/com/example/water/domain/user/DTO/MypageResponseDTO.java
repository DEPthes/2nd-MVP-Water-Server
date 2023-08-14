package com.example.water.domain.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MypageResponseDTO {
    private int status;
    private String message;
    private MypageDTO data;

}