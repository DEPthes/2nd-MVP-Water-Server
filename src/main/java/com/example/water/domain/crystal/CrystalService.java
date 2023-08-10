package com.example.water.domain.crystal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrystalService {
    private final CrystalRepository crystalRepository;

    public Optional<Crystal> findById(Long userId) {
        return crystalRepository.findById(userId);
    }
}
