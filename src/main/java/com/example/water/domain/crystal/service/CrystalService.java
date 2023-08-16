package com.example.water.domain.crystal.service;

import com.example.water.domain.crystal.dto.response.CrystalResponse;
import com.example.water.domain.crystal.entity.Crystal;
import com.example.water.domain.user.entity.User;
import com.example.water.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CrystalService {
    private final UserRepository userRepository;

    public List<CrystalResponse> getCrystalResponses(Map<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        User user = userRepository.findByEmail(email);
        List<Crystal> crystals = user.getCrystals();

        List<CrystalResponse> crystalResponses = new ArrayList<>();

        for (Crystal crystal : crystals) {
            CrystalResponse dto = CrystalResponse.builder()
                    .crystalId(crystal.getCrystalId())
                    .red(crystal.getRed())
                    .green(crystal.getGreen())
                    .blue(crystal.getBlue())
                    .build();
            crystalResponses.add(dto);
        }

        return crystalResponses;
    }
}
