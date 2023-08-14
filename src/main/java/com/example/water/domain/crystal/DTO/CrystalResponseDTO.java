package com.example.water.domain.crystal.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CrystalResponseDTO {
    private Long crystalId;
    private Long red;
    private Long green;
    private Long blue;

}
