package com.example.water.domain.crystal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CrystalResponse {
    private Long crystalId;
    private Long red;
    private Long green;
    private Long blue;

}
