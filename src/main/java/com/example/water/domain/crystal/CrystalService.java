package com.example.water.domain.crystal;

import com.example.water.domain.user.User;
import com.example.water.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CrystalService {
    private final UserRepository userRepository;
    public List<Map<String, Object>> getCrystalResponses(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        List<Crystal> crystals = user.get().getCrystals(); // User 객체의 getCrystals 메소드로 해당 사용자의 crystals get
        List<Map<String, Object>> crystalResponses = new ArrayList<>();

        for (Crystal crystal : crystals) {
            Map<String, Object> response = new HashMap<>();
            response.put("crystal_id", crystal.getCrystalId());
            response.put("red", crystal.getRed());
            response.put("green", crystal.getGreen());
            response.put("blue", crystal.getBlue());
            crystalResponses.add(response);
        }
        return crystalResponses;

    }
}
