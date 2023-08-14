package com.example.water.domain.crystal;

import com.example.water.domain.crystal.DTO.CrystalResponseDTO;
import com.example.water.domain.user.User;
import com.example.water.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CrystalService {
    private final UserRepository userRepository;

    public List<CrystalResponseDTO> getCrystalResponses(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        List<Crystal> crystals = user.get().getCrystals();
        List<CrystalResponseDTO> crystalResponses = new ArrayList<>();

        for (Crystal crystal : crystals) {
            CrystalResponseDTO dto = new CrystalResponseDTO(
                    crystal.getCrystalId(),
                    crystal.getRed(),
                    crystal.getGreen(),
                    crystal.getBlue()
            );
            crystalResponses.add(dto);
        }
        return crystalResponses;
    }
}
