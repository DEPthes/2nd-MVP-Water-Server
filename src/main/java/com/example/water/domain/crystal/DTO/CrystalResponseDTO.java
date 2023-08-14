package com.example.water.domain.crystal.DTO;

import lombok.Getter;

@Getter
public class CrystalResponseDTO {
    private Long crystalId;
    private Long red;
    private Long green;
    private Long blue;

    public CrystalResponseDTO(Long crystalId, Long red, Long green, Long blue) {
        this.crystalId = crystalId;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

}
